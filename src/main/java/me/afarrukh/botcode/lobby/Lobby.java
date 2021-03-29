package me.afarrukh.botcode.lobby;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.Serializable;

public class Lobby implements Serializable {

    private final String lobbyName;
    private final String guildId;
    private final String channelId;
    private final String creatorId;
    private final long creationTime;

    Lobby(String lobbyName, GuildMessageReceivedEvent evt) {
        this.lobbyName = lobbyName;
        Guild guild = evt.getGuild();
        this.guildId = guild.getId();
        this.channelId = guild.createTextChannel(lobbyName).complete().getId();
        this.creatorId = evt.getMember().getId();
        this.creationTime = System.currentTimeMillis();
    }

    Lobby(String lobbyName, String guildId, String channelId, String creatorId, long creationTime) {
        this.lobbyName = lobbyName;
        this.guildId = guildId;
        this.channelId = channelId;
        this.creatorId = creatorId;
        this.creationTime = creationTime;
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
}
