package com.simplechat.shared.commands;

import com.simplechat.shared.commands.user.UserCommand;

/**
 * Created by lampt on 6/24/2017.
 */
public class CommandFactory {
    private static CommandFactory INSTANCE = new CommandFactory();
    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        return INSTANCE;
    }

    public AbstractCommand getCommand(Command command) {
        switch (command) {
            case USER:
                return new UserCommand();
            default:
                return null;
        }
    }

    public AbstractCommand getCommand(String command) {
        return null;
    }
}
