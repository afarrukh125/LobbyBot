package me.afarrukh.botcode;

import me.afarrukh.botcode.commands.LatencyCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.security.auth.login.LoginException;

/**
 * @author Abdullah Farrukh
 * Created on 23/03/2020 at 16:27
 */
public class Bot {

    private static Bot instance;

    private JDA botUser;
    private CommandManager commandManager;

    private static String prefix;

    private Bot(String token) throws LoginException {
        botUser = new JDABuilder(token).addEventListeners(new MessageListener()).build();
        commandManager = new CommandManager();
        prefix = "!";

        installCommands();
    }

    /**
     * Register commands here to install into the bot.
     * Simply create a class that extends the Command abstract class, and implement the
     * execute method. Then register it here and the bot will listen out for it.
     */
    private void installCommands() {
        commandManager.register(new LatencyCommand());
    }

    public static void init(String token) throws LoginException {
        if(instance != null)
            throw new IllegalStateException("Bot has already been initialised.");
        instance = new Bot(token);
    }

    public void handleCommandEvent(GuildMessageReceivedEvent event) {
        // If the event message is, e.g. !cmd testing testing, commandName is set to "cmd"
        String commandName = event.getMessage().getContentRaw().substring(1).split(" ")[0];
        commandManager.handleCommand(commandName, event);
    }

    public static Bot getInstance() {
        if(instance == null)
            throw new IllegalStateException("Bot has not been initialised. Please use Bot#init() to create the bot");
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    public JDA getBotUser() {
        return botUser;
    }
}
