package me.afarrukh.lobbybot.commands.lobby;

import me.afarrukh.lobbybot.core.Bot;
import me.afarrukh.lobbybot.lobby.Lobby;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class CreateLobbyCommand extends LobbyCommand {
    public CreateLobbyCommand() {
        super("createlobby");
        addAlias("cl");
    }

    @Override
    public void execute(GuildMessageReceivedEvent evt, List<String> params) {
        String lobbyName;

        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        if (params.isEmpty())
            lobbyName = requireNonNull(evt.getMember()).getEffectiveName() + "s-lobby-" + dtf.format(new Date());
        else
            lobbyName = joinParams(params);

        Bot.getInstance().getLobbyManager()
                .createAndReturnLobbyWithName(lobbyName, evt)
                .ifPresentOrElse(
                        lobby -> createLobbyAndOpenChannel(evt, lobby),
                        () -> notifyLobbyAlreadyExists(evt)
                );

    }

    private void createLobbyAndOpenChannel(GuildMessageReceivedEvent evt, Lobby createdLobby) {
        evt.getAuthor()
                .openPrivateChannel()
                .queue(c -> c.sendMessage("Lobby " + createdLobby.getLobbyName() + " was created successfully")
                        .queue(m -> this.deleteMessage(evt)));
    }

    private void notifyLobbyAlreadyExists(GuildMessageReceivedEvent evt) {
        evt.getAuthor().openPrivateChannel().queue(c -> c.sendMessage("You already have an existing lobby.")
                .queue(m -> this.deleteMessage(evt)));
    }

    @Override
    public String getDescription() {
        return null;
    }
}
