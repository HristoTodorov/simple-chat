package com.simplechat.shared.commands.user;

import com.simplechat.shared.commands.AbstractCommand;
import com.simplechat.shared.commands.AbstractCommandBuilder;
import com.simplechat.shared.commands.Command;

/**
 * Created by lampt on 6/24/2017.
 */
public class UserCommand implements AbstractCommand {

    @Override
    public Command getCommandName() {
        return Command.USER;
    }

    @Override
    public AbstractCommandBuilder buildCommand(String... commandArgs) {
        return new UserCommandBuilder(commandArgs);
    }
}
