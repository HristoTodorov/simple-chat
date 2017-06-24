package com.simplechat.shared.commands;

/**
 * Created by lampt on 6/24/2017.
 */
public interface AbstractCommand {
    Command getCommandName();
    AbstractCommandBuilder buildCommand(String... commandArgs);
}
