package me.afarrukh.lobbybot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public class CommandManager {

    private final Map<String, Command> commands;

    public CommandManager() {
        commands = new HashMap<>();
    }

    public void handleCommand(String commandName, GuildMessageReceivedEvent evt) {
        commandFromName(commandName).ifPresent(command -> execute(evt, command));
    }

    public CommandManager register(Command command) {
        commands.put(command.getName().toLowerCase(), command);

        for (String alias : command.getAliases())
            commands.put(alias.toLowerCase(), command);

        return this;
    }

    private void execute(GuildMessageReceivedEvent evt, Command command) {
        String[] tokens = evt.getMessage().getContentRaw().substring(1).toLowerCase().split(" ", 2);
        List<String> paramList = new ArrayList<>();
        if (hasParams(tokens)) {
            final String params = tokens[1].trim();
            paramList = new ArrayList<>(Arrays.asList(params.split(" ")));
        }
        command.execute(evt, paramList);
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
