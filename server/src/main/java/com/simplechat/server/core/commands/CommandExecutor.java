package com.simplechat.server.core.commands;

import com.simplechat.server.core.client.Client;
import com.simplechat.server.core.client.ClientLogoutException;
import com.simplechat.server.core.client.ClientRegistry;
import com.simplechat.server.core.message.AbstractMessageNotifier;
import com.simplechat.server.core.message.MessageUtils;
import com.simplechat.server.core.message.NotifierRegistry;
import com.simplechat.server.core.response.IServerResponse;
import com.simplechat.server.core.response.NullServerResponse;
import com.simplechat.server.core.response.ServerResponse;
import com.simplechat.shared.messages.Commands;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by lampt on 6/17/2017.
 */
public class CommandExecutor implements Callable<IServerResponse> {

    private final String command;

    private final String senderName;

    public CommandExecutor(String senderName, String command) {
        this.command = command;
        this.senderName = senderName;
    }

    @Override
    public IServerResponse call() throws Exception {
        if (StringUtils.isBlank(command)) {
            return new NullServerResponse();
        }
        String[] commandArgs = CommandUtils.getCommandArgs(command);

        // TODO make commands more clear !

        // send_to command
        if (Commands.SEND_TO.equals(commandArgs[0])) {
            return executeSendToCommand(commandArgs[1], CommandUtils.getUnicastMessage(command));
        }

        // send_all command
        if (Commands.SEND_ALL.equals(commandArgs[0])) {
            return executeSendAllCommand(CommandUtils.getBroadcastMessage(command));
        }

        //list command
        if (commandArgs.length == 1 && Commands.LIST.equals(commandArgs[0])) {
            return executeListCommand();
        }

        // TODO file command

        if (commandArgs.length == 1 && Commands.BYE.equals(commandArgs[0])) {
            ClientRegistry.unregisterClient(senderName);
            NotifierRegistry.removeNotifier(senderName);
            notifyClients();
            throw new ClientLogoutException();
        }
        return new NullServerResponse();
    }

    private void notifyClients() {
        final String errorMessage = MessageUtils.prepareGoneMessage(senderName);
        NotifierRegistry.getNotifiers().forEach(
                notifyer -> notifyer.onMessage(errorMessage));
    }

    private IServerResponse executeListCommand() {
        final String connectedClients = ClientRegistry.getConnectedClients()
                .stream()
                .map(Client::getUserName)
                .collect(Collectors.joining(" "));
        return new ServerResponse(IServerResponse.ResponseCode.OK, connectedClients);
    }

    private IServerResponse executeSendAllCommand(final String message) throws InterruptedException, java.util.concurrent.ExecutionException {
        if (StringUtils.isBlank(message)) {
            return new NullServerResponse();
        }
        return CompletableFuture.supplyAsync(() -> {
            ClientRegistry.getConnectedClients().forEach((Client client) -> doSendMessage(message, client));
            return new ServerResponse(IServerResponse.ResponseCode.OK,
                    "message sent successfully.\n");
        }).get();
    }

    private IServerResponse executeSendToCommand(final String userName, final String message) {
        // send message
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(message)) {
            return new NullServerResponse();
        }
        Optional<Client> optionalClient = ClientRegistry.getClient(userName);
        if (optionalClient.isPresent()) {
            doSendMessage(message, optionalClient.get());
            return new ServerResponse(IServerResponse.ResponseCode.OK,
                    String.format("Message to %s  sent successfully.", userName));
        } else {
            return new ServerResponse(IServerResponse.ResponseCode.ERROR,
                    String.format("%s does not exist!", userName));
        }
    }

    private void doSendMessage(String message, @NotNull Client recipient) {
        AbstractMessageNotifier listener = NotifierRegistry.getNotifier(recipient.getUserName());
        listener.onMessage(MessageUtils.prepareRecievedMessage(senderName, message)); // it is ugly, but who cares
    }
}
