package me.afarrukh.botcode.lobby;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class DefaultLobbyEventHandler implements LobbyEventHandler {

    private final LobbyManager lobbyManager;

    public DefaultLobbyEventHandler(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    @Override
    public void handleReactionAddEvent(GuildMessageReactionAddEvent evt) {
        // TODO implement
    }

    @Override
    public void handleVoiceJoinEvent(GuildVoiceJoinEvent evt) {
        // TODO implement
    }
}
