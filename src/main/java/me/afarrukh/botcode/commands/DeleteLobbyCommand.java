package me.afarrukh.botcode.commands;

import me.afarrukh.botcode.Bot;
import me.afarrukh.botcode.Command;
import me.afarrukh.botcode.lobby.Lobby;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Optional;

public class DeleteLobbyCommand extends Command {

    public DeleteLobbyCommand() {
        super("deletelobby");
        addAlias("dl");
    }


    @Override
    public void execute(GuildMessageReceivedEvent evt, List<String> params) {
        final Optional<Lobby> result = Bot.getInstance().getLobbyManager().deleteLobby(evt);
        if(result.isPresent()) {
            Lobby deletedLobby = result.get();
            evt.getAuthor()
                    .openPrivateChannel()
                    .queue(c -> c.sendMessage("Lobby " + deletedLobby.getLobbyName() + " was deleted successfully").queue());
        }
        else
            evt.getAuthor().openPrivateChannel().queue(c -> c.sendMessage("You do not currently have a lobby already").queue());
    }

    @Override
    public String getDescription() {
        return null;
    }
}
