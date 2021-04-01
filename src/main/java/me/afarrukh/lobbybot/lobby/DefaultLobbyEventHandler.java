package me.afarrukh.lobbybot.lobby;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A default implementation of {@link LobbyEventHandler} that controls what happens to the
 * associated data structures, channels, and roles when members of a Discord guild interact with
 * the bot.
 */
public class DefaultLobbyEventHandler implements LobbyEventHandler {

    private final LobbyManager lobbyManager;
    private final Map<Lobby, LobbyMessageTable> pendingMessages;

    private static final String CHECK_EMOTE = "\u2611";
    private static final String CROSS_EMOTE = "\u274C";

    public DefaultLobbyEventHandler(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
        pendingMessages = new HashMap<>();
    }

    @Override
    public void handleReactionAddEvent(GuildMessageReactionAddEvent evt) {
        String channelId = evt.getChannel().getId();
        String memberId = evt.getMember().getId();

        Optional<Lobby> lobbyOptional = lobbyManager.getLobbyForUser(memberId);
        if (lobbyOptional.isPresent()) {
            Lobby lobby = lobbyOptional.get();
            LobbyMessageTable table = pendingMessages.get(lobby);
            Optional<Message> reactedMessage = table.findMessageById(evt.getMessageId());
            if (reactedMessage.isPresent()) {
                Message message = reactedMessage.get();
                Optional<Member> targetMemberOptional = table.getMemberForMessage(message);
                if (lobby.getChannelId().equals(channelId) && lobby.getCreatorId().equals(memberId) && targetMemberOptional.isPresent()) {
                    Member member = targetMemberOptional.get();
                    if (lobby.getMemberIds().contains(member.getId()))
                        return;
                    switch (evt.getReaction().getReactionEmote().getEmoji()) {
                        case CHECK_EMOTE:
                            lobby.addMember(member);
                        case CROSS_EMOTE:
                            table.removeMessageForMember(member);
                    }
                }
            }
        }
    }

    @Override
    public void handleVoiceJoinEvent(GuildVoiceJoinEvent evt) {
        Member newMember = evt.getMember();
        List<Member> channelMembers = evt.getChannelJoined().getMembers();
        if (channelMembers != null) {
            for (Member member : channelMembers) {
                if (member.equals(newMember))
                    continue;
                Optional<Lobby> lobbyResult = lobbyManager.getLobbyForUser(member.getId());
                if (lobbyResult.isPresent()) {
                    Lobby lobby = lobbyResult.get();
                    if (lobby.getMemberIds().contains(newMember.getId()))
                        return;
                    LobbyMessageTable table = pendingMessages.get(lobby);
                    if (table == null) {
                        table = new LobbyMessageTable();
                        pendingMessages.put(lobby, table);
                    }
                    MessageEmbed messageEmbed = generateJoinEmbed(evt);

                    LobbyMessageTable finalTable = table;
                    evt.getGuild().getTextChannelById(lobby.getChannelId()).sendMessage(messageEmbed).queue(
                            m -> {
                                finalTable.addPendingMessage(newMember, m);
                                m.addReaction(CHECK_EMOTE).queue();
                                m.addReaction(CROSS_EMOTE).queue();
                            }
                    );
                }
            }
        }
    }

    private MessageEmbed generateJoinEmbed(GuildVoiceJoinEvent evt) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(evt.getMember().getEffectiveName() + " would like to join your lobby.");
        eb.setThumbnail(evt.getMember().getUser().getAvatarUrl());
        return eb.build();
    }

    @Override
    public void handleVoiceMovedEvent(GuildVoiceMoveEvent evt) {
        List<Member> members = evt.getChannelLeft().getMembers();
        handleRemoval(evt, members);
    }

    @Override
    public void handleVoiceLeftEvent(GuildVoiceLeaveEvent evt) {
        List<Member> members = evt.getChannelLeft().getMembers();
        handleRemoval(evt, members);
    }

    private void handleRemoval(GenericGuildVoiceEvent evt, List<Member> members) {
        for (Member member : members) {
            Optional<Lobby> lobbyResult = lobbyManager.getLobbyForUser(member.getId());
            if (lobbyResult.isPresent()) {
                Lobby lobby = lobbyResult.get();
                if (lobby.getMemberIds().contains(evt.getMember().getId()))
                    lobby.removeMember(evt.getMember());
                if (pendingMessages.get(lobby) != null)
                    pendingMessages.get(lobby).removeMessageForMember(evt.getMember());
            }
        }
    }
}
