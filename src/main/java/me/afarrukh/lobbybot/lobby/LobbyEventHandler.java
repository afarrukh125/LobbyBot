package me.afarrukh.lobbybot.lobby;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public interface LobbyEventHandler {
    void handleReactionAddEvent(GuildMessageReactionAddEvent evt);
    void handleVoiceJoinEvent(GuildVoiceJoinEvent evt);

    void handleVoiceMovedEvent(GuildVoiceMoveEvent evt);

    void handleVoiceLeftEvent(GuildVoiceLeaveEvent evt);
}
