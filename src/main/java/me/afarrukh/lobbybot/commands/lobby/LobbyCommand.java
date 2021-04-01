package me.afarrukh.lobbybot.commands.lobby;

import me.afarrukh.lobbybot.commands.Command;
import me.afarrukh.lobbybot.lobby.Lobby;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

abstract class LobbyCommand extends Command {

    public LobbyCommand(String name) {
        super(name);
    }

    protected void deleteMessage(GuildMessageReceivedEvent evt, Lobby lobby) {
        try {
            if (!evt.getChannel().getId().equals(lobby.getChannelId()))
                evt.getMessage().delete().queue();
        } catch (ErrorResponseException ignored) {
        }
    }

    protected void deleteMessage(GuildMessageReceivedEvent evt) {
        evt.getMessage().delete().queue();
    }
}
