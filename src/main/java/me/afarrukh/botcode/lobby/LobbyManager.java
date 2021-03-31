package me.afarrukh.botcode.lobby;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbyManager {

    private static final String FILE_NAME = "lobbies.dat";

    private Map<String, Lobby> lobbies;

    @SuppressWarnings("unchecked")
    public LobbyManager() {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(FILE_NAME));
            lobbies = (Map<String, Lobby>) in.readObject();
        } catch (IOException e) {
            lobbies = new ConcurrentHashMap<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Lobby getLobbyForUser(String userName) {
        return lobbies.get(userName);
    }

    public Optional<Lobby> createLobby(String lobbyName, GuildMessageReceivedEvent evt) {
        String memberId = evt.getMember().getId();
        if (!lobbies.containsKey(memberId)) {
            Lobby lobby = new Lobby(lobbyName, evt);
            lobbies.put(memberId, lobby);
            serializeToFile();
            return Optional.of(lobby);
        }
        return Optional.empty();

    }

    public Optional<Lobby> deleteLobby(GuildMessageReceivedEvent evt) {
        String memberId = evt.getMember().getId();
        Guild guild = evt.getGuild();
        Lobby lobby = lobbies.get(memberId);

        if (lobby != null) {
            lobbies.remove(memberId);
            guild.getTextChannelById(lobby.getChannelId()).delete().queue();
            serializeToFile();
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    private void serializeToFile() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> {
            try {
                FileOutputStream fout = new FileOutputStream(FILE_NAME);
                ObjectOutputStream out = new ObjectOutputStream(fout);
                out.writeObject(lobbies);
                out.flush();
                //closing the stream
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }

}
