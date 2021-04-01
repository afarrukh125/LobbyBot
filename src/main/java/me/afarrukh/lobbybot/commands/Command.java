package me.afarrukh.lobbybot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

public abstract class Command {

    protected String name;
    protected List<String> parameters;
    protected Set<String> aliases;

    public Command(String name) {
        this.name = name;
        this.parameters = new ArrayList<>(3);
        this.aliases = new HashSet<>();
    }

    public Command addAlias(String alias) {
        aliases.add(alias);
        return this;
    }

    /**
     * Executes the command given the context (event)
     * @param evt The event context to execute according to
     * @param params Potential parameters for this command execution
     */
    public abstract void execute(GuildMessageReceivedEvent evt, List<String> params);

    public String getName() {
        return name;
    }

    /**
     * Returns a description for this command
     * @return A string corresponding to a description of this command
     */
    public abstract String getDescription();

    /**
     * Returns all parameters joined by a separator
     * For example, ["Hi, "How", "Are", "You"] is converted to "Hi How Are You" with a space (" ") separator
     * @param params The parameters to join with the separator
     * @param separator The separator to join with
     * @return A string that represents the parameters, separated by the separator/delimiter
     */
    protected static String joinParams(List<String> params, String separator) {
        StringBuilder sb = new StringBuilder();
        for(String param : params)
            sb.append(param).append(separator);
        return sb.toString().trim();
    }

    protected static String joinParams(List<String> params) {
        return joinParams(params, " ");
    }

    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    /**
     * @return A list of aliases of this command
     */
    public Set<String> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }
}
