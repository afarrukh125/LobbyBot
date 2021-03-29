package me.afarrukh.botcode.commands;

import me.afarrukh.botcode.Bot;
import me.afarrukh.botcode.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(GuildMessageReceivedEvent evt, List<String> params) {
        evt.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(getHelpEmbed()).queue());
    }

    private static MessageEmbed getHelpEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Help");
        StringBuilder sb = new StringBuilder();
        for(Command command : Bot.getInstance().getCommands())
            sb.append("`").append(command.getName()).append("` - ").append(command.getDescription()).append("\n\n");
        eb.setDescription(sb);
        return eb.build();
    }

    @Override
    public String getDescription() {
        return null;
    }
}
