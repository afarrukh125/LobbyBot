package me.afarrukh.lobbybot.commands.lobby;

import me.afarrukh.lobbybot.core.Bot;
import me.afarrukh.lobbybot.lobby.Lobby;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class DeleteLobbyCommand extends LobbyCommand {

    public DeleteLobbyCommand() {
        super("deletelobby");
        addAlias("dl");
    }


    @Override
    public void execute(GuildMessageReceivedEvent evt, List<String> params) {
        Bot.getInstance()
                .getLobbyManager()
                .deleteLobby(evt)
                .ifPresentOrElse(
                        deletedLobby -> deleteLobbyAndNotifyUser(evt, deletedLobby),
                        () -> notifyUserOfFailedLobbyDeletion(evt)
                );
    }

    private void deleteLobbyAndNotifyUser(GuildMessageReceivedEvent evt, final Lobby deletedLobby) {
        evt.getAuthor().openPrivateChannel()
                .queue(c -> c.sendMessage("Lobby " + deletedLobby.getLobbyName() + " was deleted successfully")
                        .queue(m -> this.deleteMessage(evt, deletedLobby)));
    }

    private void notifyUserOfFailedLobbyDeletion(GuildMessageReceivedEvent evt) {
        evt.getAuthor().openPrivateChannel()
                .queue(c -> c.sendMessage("You do not currently have a lobby already")
                        .queue(m -> this.deleteMessage(evt)));
    }

    @Override
    public String getDescription() {
        return null;
    }
}
