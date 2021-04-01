package me.afarrukh.lobbybot.lobby;

import me.afarrukh.lobbybot.core.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

import java.io.Serializable;
import java.util.*;

public class Lobby implements Serializable {

    private final String lobbyName;
    private final String guildId;
    private final String channelId;
    private final String creatorId;
    private final long creationTime;
    Set<String> memberIds;
    private final String roleId;

    private static final List<Permission> BOT_PERMISSIONS = Arrays.asList(
            Permission.VIEW_CHANNEL,
            Permission.MESSAGE_WRITE,
            Permission.MESSAGE_ADD_REACTION,
            Permission.MESSAGE_READ
    );

    private static final List<Permission> MEMBER_PERMISSIONS = Arrays.asList(
            Permission.VIEW_CHANNEL,
            Permission.MESSAGE_ADD_REACTION,
            Permission.MESSAGE_WRITE
    );

    Lobby(String lobbyName, GuildMessageReceivedEvent evt) {
        this.lobbyName = lobbyName;
        Guild guild = evt.getGuild();
        this.guildId = guild.getId();
        this.creatorId = evt.getMember().getId();
        this.creationTime = System.currentTimeMillis();
        memberIds = new HashSet<>();

        Member botMember = guild.getMember(Bot.getInstance().getBotUser().getSelfUser());

        RoleAction associatedRole = guild.createRole().setMentionable(false);
        Role role = associatedRole.setName(System.currentTimeMillis()+"").complete();
        this.roleId = role.getId();

        // Create and setup permissions for public, role and bot
        TextChannel channel = guild.createTextChannel(lobbyName)
                .addPermissionOverride(guild.getPublicRole(), null, Collections.singletonList(Permission.VIEW_CHANNEL))
                .addMemberPermissionOverride(botMember.getIdLong(), BOT_PERMISSIONS, null)
                .addRolePermissionOverride(role.getIdLong(), MEMBER_PERMISSIONS, null)
                .complete();

        this.channelId = channel.getId();

        guild.addRoleToMember(evt.getMember(), role).queue();

        memberIds.add(creatorId);
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getRoleId() {
        return roleId;
    }

    public Set<String> getMemberIds() {
        return Collections.unmodifiableSet(memberIds);
    }

    Role getRole() {
        return Bot.getInstance().getBotUser().getGuildById(guildId).getRoleById(roleId);
    }

    Guild getGuild() {
        return Bot.getInstance().getBotUser().getGuildById(guildId);
    }

    public void addMember(Member member) {
        Guild guild = getGuild();
        Role role = getRole();
        guild.addRoleToMember(member, role).queue();
        memberIds.add(member.getId());
    }

    public void removeMember(Member member) {
        memberIds.remove(member.getId());
        Guild guild = getGuild();
        Role role = getRole();
        guild.removeRoleFromMember(member, role).queue();
    }
}
