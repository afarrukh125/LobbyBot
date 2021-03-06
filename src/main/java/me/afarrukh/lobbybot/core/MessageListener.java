package me.afarrukh.lobbybot.core;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        if (containsCommand(event))
            Bot.getInstance().handleCommandEvent(event);
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent evt) {
        Bot.getInstance().getLobbyManager().handleVoiceMovedEvent(evt);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent evt) {
        Bot.getInstance().getLobbyManager().handleVoiceLeftEvent(evt);
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent evt) {
        Bot.getInstance().getLobbyManager().handleVoiceJoinEvent(evt);
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent evt) {
        if (!evt.getUser().isBot())
            Bot.getInstance().getLobbyManager().handleReactionAddEvent(evt);
    }

    private boolean containsCommand(GuildMessageReceivedEvent evt) {
        return evt.getMessage().getContentRaw().startsWith(Bot.getInstance().getPrefix());
    }
}
