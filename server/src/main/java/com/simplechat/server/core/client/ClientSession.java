package com.simplechat.server.core.client;

import com.simplechat.server.core.commands.CommandExecutor;
import com.simplechat.server.core.commands.CommandUtils;
import com.simplechat.server.core.message.ClientMessageNotifier;
import com.simplechat.server.core.message.NotifierRegistry;
import com.simplechat.server.core.response.IServerResponse;
import com.simplechat.server.core.response.NullServerResponse;
import com.simplechat.server.core.response.ServerResponse;
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
        try (Reader reader = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader in = new BufferedReader(reader);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {
            final SocketAddress socketAddress = clientSocket.getRemoteSocketAddress();
            System.out.println(MessageFormat.format("Unknown user try to login from {0}. ",
                    socketAddress));
            final String userName = CommandUtils.processLoginCommand(in.readLine());
            if (StringUtils.isBlank(userName)) {
                out.println(new ServerResponse(IServerResponse.ResponseCode.ERROR,
                        "Invalid user name. Please specify a correct username!").toString());
                out.flush();
                return;
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
            } else {
                out.println(new ServerResponse(IServerResponse.ResponseCode.ERROR,
                        String.format("%s already taken!", userName)).toString());
                out.flush();
                return;
            }
            processClientSession(in, out, userName, client);
        } catch (Throwable ex) {
            System.err.println(String.format("Connection from %s reset!",
                    clientSocket.getRemoteSocketAddress().toString()));
            ex.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                ClientRegistry.unregisterClient(clientSocket.getRemoteSocketAddress());
                System.out.println(String.format("%s is stopped!",
                        clientSocket.getRemoteSocketAddress().toString()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void processClientSession(@NotNull BufferedReader in, @NotNull PrintWriter out, String userName, @NotNull Client client) throws IOException {
        // Client
        while (client.isRegistered()) {
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
                        e.printStackTrace();
                        return new NullServerResponse();
                    }
                }
            }).thenAcceptAsync(serverResponse -> {
                out.println(serverResponse.toString());
                out.flush();
            });
        }
    }
}
