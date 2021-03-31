package me.afarrukh.botcode.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

/**
 * @author Abdullah Farrukh
 * Created on 23/03/2020 at 16:17
 */
public class CommandManager {

    private Map<String, Command> commands;

    public CommandManager() {
        commands = new HashMap<>();
    }

    public void handleCommand(String commandName, GuildMessageReceivedEvent event) {
        Optional<Command> commandOptional = commandFromName(commandName);

        // Adds any space separated strings to the parameter list
        commandOptional.ifPresent(command -> {
            String[] tokens = event.getMessage().getContentRaw().substring(1).toLowerCase().split(" ", 2);
            List<String> paramList = new ArrayList<>();
            if(hasParams(tokens)) {
                final String params = tokens[1].trim();
                paramList = new ArrayList<>(Arrays.asList(params.split(" ")));
            }
            command.execute(event, paramList);
        });
    }

    public CommandManager register(Command command) {
        commands.put(command.getName().toLowerCase(), command);

        for(String alias : command.getAliases())
            commands.put(alias.toLowerCase(), command);

        return this;
    }

    public Collection<Command> getCommands() {
        return Collections.unmodifiableCollection(commands.values());
    }

    private boolean hasParams(String[] tokens) {
        return tokens.length > 1;
    }

    public Optional<Command> commandFromName(String name) {
        return Optional.ofNullable(commands.get(name));
    }
}
