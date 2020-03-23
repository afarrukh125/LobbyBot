package me.afarrukh.botcode;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * @author Abdullah Farrukh
 * Created on 23/03/2020 at 16:32
 */
public class MessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot())
            return;

        if(containsCommand(event))
            Bot.getInstance().handleCommandEvent(event);

    }

    private boolean containsCommand(GuildMessageReceivedEvent event) {
        return event.getMessage().getContentRaw().startsWith(Bot.getInstance().getPrefix());
    }
}
