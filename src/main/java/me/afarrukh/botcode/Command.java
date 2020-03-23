package me.afarrukh.botcode;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;

/**
 * @author Abdullah Farrukh
 * Created on 23/03/2020 at 16:27
 */
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
     * @param event The event context to execute according to
     * @param params Potential parameters for this command execution
     */
    public abstract void execute(GuildMessageReceivedEvent event, List<String> params);

    public String getName() {
        return name;
    }

    /**
     * Returns a description for this command
     * @return A string corresponding to a description of this command
     */
    public abstract String getDescription();

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
