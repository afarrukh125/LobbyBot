package me.afarrukh.botcode.lobby;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class LobbyManager {

    private final Map<String, Lobby> lobbies;

    public LobbyManager() {
        lobbies = new HashMap<>();
    }

    public Lobby getLobbyForUser(String userName) {
        return lobbies.get(lobbies);
    }

    public void createLobby(String lobbyname, GuildMessageReceivedEvent evt) {
        lobbies.put(evt.getMember().getId(), new Lobby(lobbyname, evt));
    }


}
