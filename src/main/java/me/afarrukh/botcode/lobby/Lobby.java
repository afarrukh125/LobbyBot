package me.afarrukh.botcode.lobby;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Lobby implements Serializable {

    private final String lobbyName;
    private final String guildId;
    private final String channelId;
    private final String creatorId;
    private final long creationTime;

    Set<String> memberIds;

    private final String roleId;

    Lobby(String lobbyName, GuildMessageReceivedEvent evt) {
        this.lobbyName = lobbyName;
        Guild guild = evt.getGuild();
        this.guildId = guild.getId();
        this.creatorId = evt.getMember().getId();
        this.creationTime = System.currentTimeMillis();
        memberIds = new HashSet<>();

        TextChannel channel = guild.createTextChannel(lobbyName).complete();
        this.channelId = channel.getId();

        RoleAction associatedRole = guild.createRole().setMentionable(false);
        this.roleId = associatedRole.setName(System.currentTimeMillis()+"").complete().getId();

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
}
