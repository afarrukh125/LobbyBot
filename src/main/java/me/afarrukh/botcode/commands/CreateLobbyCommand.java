package me.afarrukh.botcode.commands;

import me.afarrukh.botcode.Bot;
import me.afarrukh.botcode.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CreateLobbyCommand extends Command {
    public CreateLobbyCommand() {
        super("createlobby");
        addAlias("cl");
    }

    @Override
    public void execute(GuildMessageReceivedEvent evt, List<String> params) {
        String lobbyName;

        if(params.isEmpty()) {
            lobbyName = System.currentTimeMillis() + evt.getMember().getEffectiveName() + "lobby";
        } else
            lobbyName = joinParams(params);

        Bot.getInstance().getLobbyManager();

    }

    @Override
    public String getDescription() {
        return null;
    }
}
