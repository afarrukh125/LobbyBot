package me.afarrukh.botcode.lobby;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public interface LobbyEventHandler {
    void handleReactionAddEvent(GuildMessageReactionAddEvent evt);
    void handleVoiceJoinEvent(GuildVoiceJoinEvent evt);
}
