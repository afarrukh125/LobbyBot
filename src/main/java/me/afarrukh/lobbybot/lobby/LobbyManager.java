package me.afarrukh.lobbybot.lobby;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbyManager implements LobbyEventHandler {

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

    public Optional<Lobby> getLobbyForUser(String userId) {
        if (lobbies.containsKey(userId))
            return Optional.of(lobbies.get(userId));
        return Optional.empty();
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

    @Override
    public void handleVoiceJoinEvent(GuildVoiceJoinEvent evt) {
        lobbyEventHandler.handleVoiceJoinEvent(evt);
    }

    @Override
    public void handleVoiceLeftEvent(GuildVoiceLeaveEvent evt) {
        lobbyEventHandler.handleVoiceLeftEvent(evt);
    }

    @Override
    public void handleReactionAddEvent(GuildMessageReactionAddEvent evt) {
        lobbyEventHandler.handleReactionAddEvent(evt);
    }

    @Override
    public void handleVoiceMovedEvent(GuildVoiceMoveEvent evt) {
        lobbyEventHandler.handleVoiceMovedEvent(evt);
    }
}
