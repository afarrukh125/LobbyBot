package me.afarrukh.botcode.commands;

import me.afarrukh.botcode.Bot;
import me.afarrukh.botcode.Command;
import me.afarrukh.botcode.lobby.Lobby;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.Optional;

public class CreateLobbyCommand extends Command {
    public CreateLobbyCommand() {
        super("createlobby");
        addAlias("cl");
    }

    @Override
    public void execute(GuildMessageReceivedEvent evt, List<String> params) {
        String lobbyName;

        if(params.isEmpty())
            lobbyName = evt.getMember().getEffectiveName() + "'s-lobby" + System.currentTimeMillis();
        else
            lobbyName = joinParams(params);

        Optional<Lobby> result = Bot.getInstance().getLobbyManager().createLobby(lobbyName, evt);
        if(result.isPresent()) {
            Lobby createdLobby = result.get();
            evt.getAuthor()
                    .openPrivateChannel()
                    .queue(c -> c.sendMessage("Lobby " + createdLobby.getLobbyName() + " was created successfully").queue());
        }
        else
            evt.getAuthor().openPrivateChannel().queue(c -> c.sendMessage("You already have an existing lobby.").queue());
    }

    @Override
    public String getDescription() {
        return null;
    }
}
