package me.afarrukh.botcode;

import javax.security.auth.login.LoginException;

/**
 * @author Abdullah Farrukh
 * Created on 23/03/2020 at 17:13
 */
public class Main {

    public static void main(String[] args) throws LoginException {
        if(args.length == 0)
            throw new IllegalArgumentException("Please enter a bot token");
        Bot.init(args[0]);
    }
}