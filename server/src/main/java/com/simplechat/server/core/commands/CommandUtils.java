package com.simplechat.server.core.commands;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandUtils {

    private CommandUtils() {
    }

    private static final String UNICAST_MESSAGE_PATTERN = "({0})( )(\\w*)(.*)";

    private static final String BROADCAST_MESSAGE_PATTERN = "({0})( )(.*)";

    private static final Pattern UNICAST_PATTERN = Pattern.compile(
            MessageFormat.format(UNICAST_MESSAGE_PATTERN, Commands.SEND_TO));

    private static final Pattern BROADCAST_PATTERN = Pattern.compile(
            MessageFormat.format(BROADCAST_MESSAGE_PATTERN, Commands.SEND_ALL));

    public static String processLoginCommand(@NotNull final String command) {
        if (StringUtils.isBlank(command)) {
            return null;
        }
        String[] commandArgs = getCommandArgs(command);
        if (commandArgs.length == 2 && Commands.USER.equals(commandArgs[0])) {
            return commandArgs[1];
        } else {
            return null;
        }
    }

    public static String[] getCommandArgs(@NotNull String command) {
        return command.replace("\r", "")
                .replace("\n", "")
                .split(" ");
    }

    public static String getUnicastMessage(String command) {
        Matcher matcher = UNICAST_PATTERN.matcher(command);
        if (matcher.matches()) {
            return matcher.group(4);
        }
        return null;
    }

    public static String getBroadcastMessage(String command) {
        Matcher matcher = BROADCAST_PATTERN.matcher(command);
        if (matcher.matches()) {
            return matcher.group(3);
        }
        return null;
    }
}
