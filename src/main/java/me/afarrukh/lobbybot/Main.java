package me.afarrukh.lobbybot;

import me.afarrukh.lobbybot.core.Bot;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) throws LoginException, InterruptedException {
        if(args.length == 0)
            throw new IllegalArgumentException("Please enter a bot token");
        Bot.init(args[0]);
    }
}
