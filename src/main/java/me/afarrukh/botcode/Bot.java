package me.afarrukh.botcode;

import me.afarrukh.botcode.commands.lobby.CreateLobbyCommand;
import me.afarrukh.botcode.commands.lobby.DeleteLobbyCommand;
import me.afarrukh.botcode.commands.HelpCommand;
import me.afarrukh.botcode.lobby.LobbyManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Collection;

/**
 * @author Abdullah Farrukh
 * Created on 23/03/2020 at 16:27
 */
public class Bot {

    private static Bot instance;

    private JDA botUser;
    private CommandManager commandManager;
    private LobbyManager lobbyManager;

    private static String prefix;

    private Bot(String token) throws LoginException, InterruptedException {
        botUser = JDABuilder.create(token, GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MESSAGES)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS)
                .addEventListeners(new MessageListener())
                .build().awaitReady();
        commandManager = new CommandManager();
        prefix = "!";

        lobbyManager = new LobbyManager();
        installCommands();
    }

    /**
     * Register commands here to install into the bot.
     * Simply create a class that extends the Command abstract class, and implement the
     * execute method. Then register it here and the bot will listen out for it.
     */
    private void installCommands() {
        commandManager.register(new CreateLobbyCommand())
                      .register(new DeleteLobbyCommand())
                      .register(new HelpCommand());
    }

    public static void init(String token) throws LoginException, InterruptedException {
        if(instance != null)
            throw new IllegalStateException("Bot has already been initialised.");
        instance = new Bot(token);
    }

    public void handleCommandEvent(GuildMessageReceivedEvent event) {
        // If the event message is, e.g. !cmd testing testing, commandName is set to "cmd"
        String commandName = event.getMessage().getContentRaw().substring(1).split(" ")[0].toLowerCase();
        commandManager.handleCommand(commandName, event);
    }

    public static Bot getInstance() {
        if(instance == null)
            throw new IllegalStateException("Bot has not been initialised. Please use Bot#init() to create the bot");
        return instance;
    }

    public Collection<Command> getCommands() {
        return commandManager.getCommands();
    }

    public String getPrefix() {
        return prefix;
    }

    public JDA getBotUser() {
        return botUser;
    }

    public Bot registerCommand(Command command) {
        commandManager.register(command);
        return this;
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}
