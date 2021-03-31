package me.afarrukh.botcode.commands.lobby;

import me.afarrukh.botcode.Bot;
import me.afarrukh.botcode.Command;
import me.afarrukh.botcode.lobby.Lobby;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CreateLobbyCommand extends LobbyCommand {
    public CreateLobbyCommand() {
        super("createlobby");
        addAlias("cl");
    }

    @Override
    public void execute(GuildMessageReceivedEvent evt, List<String> params) {
        String lobbyName;

        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        if(params.isEmpty())
            lobbyName = evt.getMember().getEffectiveName() + "s-lobby-" + dtf.format(new Date());
        else
            lobbyName = joinParams(params);

        Optional<Lobby> result = Bot.getInstance().getLobbyManager().createLobby(lobbyName, evt);
        if(result.isPresent()) {
            Lobby createdLobby = result.get();
            evt.getAuthor()
                    .openPrivateChannel()
                    .queue(c -> c.sendMessage("Lobby " + createdLobby.getLobbyName() + " was created successfully")
                            .queue(m -> this.deleteMessage(evt)));
        }
        else
            evt.getAuthor().openPrivateChannel().queue(c -> c.sendMessage("You already have an existing lobby.")
                    .queue(m -> this.deleteMessage(evt)));
    }

    @Override
    public String getDescription() {
        return null;
    }
}
