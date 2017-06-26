package com.simplechat.server.core.client;

import com.simplechat.server.core.commands.CommandExecutor;
import com.simplechat.server.core.commands.CommandUtils;
import com.simplechat.server.core.message.ClientMessageNotifier;
import com.simplechat.server.core.message.MessageUtils;
import com.simplechat.server.core.message.NotifierRegistry;
import com.simplechat.server.core.response.IServerResponse;
import com.simplechat.server.core.response.NullServerResponse;
import com.simplechat.server.core.response.ServerResponse;
import com.simplechat.shared.messages.Commands;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

/**
 * Created by lampt on 6/17/2017.
 */
public class ClientSession implements Runnable {

    private final Socket clientSocket;

    public ClientSession(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            Reader reader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader in = new BufferedReader(reader);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            final SocketAddress socketAddress = clientSocket.getRemoteSocketAddress();
            System.out.println(MessageFormat.format("Unknown user try to login from {0}. ",
                    socketAddress));
            final String userName = CommandUtils.processLoginCommand(in.readLine());
            CompletableFuture
                    .supplyAsync(() -> logClient(userName, out, socketAddress))
                    .thenAccept((client) -> {
                        try {
                            processClientSession(in, out, userName, client);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Throwable ex) {
            System.err.println(String.format("Connection from %s reset!",
                    clientSocket.getRemoteSocketAddress().toString()));
            logout(null);
        }
    }

    private void logout(Client client) {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientRegistry.unregisterClient(clientSocket.getRemoteSocketAddress());
        System.out.println(String.format("%s is stopped!",
                    clientSocket.getRemoteSocketAddress().toString()));
        if (client != null) {
            client.setRegistered(false);
        }
        // notify other clients for this logout
        ClientRegistry.getConnectedClients().forEach((connectedClient ->
                CommandUtils.doSendMessage(MessageUtils.prepareGoneMessage(
                        client.getUserName()), connectedClient)));
    }

    private Client logClient(final String userName, final PrintWriter out, final SocketAddress socketAddress) {
        if (StringUtils.isBlank(userName)) {
            out.println(new ServerResponse(IServerResponse.ResponseCode.ERROR,
                    "Invalid user name. Please specify a correct username!").toString());
            out.flush();
            return null;
        }
        final Client client = new ClientBean(userName, socketAddress);
        ClientRegistry.registerClient(client);
        if (client.isRegistered()) {
            System.out.println(String.format("Granting access to user %s from %s.",
                    userName, socketAddress.toString()));
            client.setRegistered(true);
            NotifierRegistry.registerNotifier(userName, new ClientMessageNotifier(out));
            out.println(new ServerResponse(IServerResponse.ResponseCode.OK,
                    String.format("%s successfully registered!", userName)).toString());
            out.flush();
            return client;
        } else {
            out.println(new ServerResponse(IServerResponse.ResponseCode.ERROR,
                    String.format("%s already taken!", userName)).toString());
            out.flush();
            return null;
        }
    }

    private void processClientSession(@NotNull BufferedReader in, @NotNull PrintWriter out, String userName,
                                      @NotNull Client client) throws IOException {
        // Client session
        while (client.isRegistered()) {
            try {
                final String message = in.readLine();
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return new CommandExecutor(userName, message).call();
                    } catch (Exception e) {
                        client.setRegistered(false);
                        if (e instanceof ClientLogoutException) {
                            return new ServerResponse(ServerResponse.ResponseCode.OK,
                                    "Logged out");
                        } else {
                            return new NullServerResponse();
                        }
                    }
                }).thenAccept(response -> {
                    out.println(response.toString());
                    out.flush();
                });
            } catch (Throwable ioe) {
                System.err.println(String.format("Connection from %s reset!",
                        clientSocket.getRemoteSocketAddress().toString()));
                logout(client);
            }
        }
    }
}
