package me.afarrukh.botcode.core;

import me.afarrukh.botcode.core.Bot;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent evt) {
        Bot.getInstance().getLobbyManager().handleVoiceJoinEvent(evt);
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent evt) {
        if(!evt.getUser().isBot())
            Bot.getInstance().getLobbyManager().handleReactionAddEvent(evt);
    }

    private boolean containsCommand(GuildMessageReceivedEvent event) {
        return event.getMessage().getContentRaw().startsWith(Bot.getInstance().getPrefix());
    }
}
