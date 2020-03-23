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

    public abstract void execute(GuildMessageReceivedEvent event, List<String> params);

    public String getName() {
        return name;
    }

    public abstract String getDescription();

    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public Set<String> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }
}
