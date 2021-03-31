package me.afarrukh.botcode.lobby;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class LobbyManager {

    private static final String FILE_NAME = "lobbies.dat";

    private final LobbyEventHandler lobbyEventHandler;

    private Map<String, Lobby> lobbies;

    @SuppressWarnings("unchecked")
    public LobbyManager() {
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(FILE_NAME));
            lobbies = (Map<String, Lobby>) inputStream.readObject();
        } catch (IOException e) {
            lobbies = new ConcurrentHashMap<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        lobbyEventHandler = new DefaultLobbyEventHandler(this);
    }

    public Lobby getLobbyForUser(String userName) {
        return lobbies.get(userName);
    }

    Collection<Lobby> getAllLobbiesForGuild(Guild guild) {
        // TODO Chicken and the egg problem - decide which lobbies to trigger the reaction message to first
        // perhaps add all members in the vc if the user is in one to the list of members already on lobby creation
        // then subsequent members will have to be added via reaction

        return lobbies.values()
                .stream()
                .filter(lobby -> lobby.getGuildId().equals(guild.getId())).collect(Collectors.toList());
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
            guild.getRoleById(lobby.getRoleId()).delete().queue();
            serializeToFile();
            return Optional.of(lobby);
        }
        return Optional.empty();
    }

    private void serializeToFile() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> {
            try {
                FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(lobbies);
                objectOutputStream.flush();
                //closing the stream
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
    }

    public void handleVoiceJoinEvent(GuildVoiceJoinEvent evt) {
        lobbyEventHandler.handleVoiceJoinEvent(evt);
    }

    public void handleReactionAddEvent(GuildMessageReactionAddEvent evt) {
        lobbyEventHandler.handleReactionAddEvent(evt);
    }
}
