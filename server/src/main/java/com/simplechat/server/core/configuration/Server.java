package com.simplechat.server.core.configuration;

import com.simplechat.server.core.client.ClientSession;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Server {

    private static final boolean SERVER_UP = true;

    private static int PORT;

    private static boolean SILENT_MODE_ON;

    public static void init(int port, boolean silentModeOn) {
        try {
            PORT = port;
            SILENT_MODE_ON = silentModeOn;
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println(String.format("Server accept requests on %d port.", PORT));
            //Runtime.getRuntime().availableProcessors()
            Executor exec = Executors.newCachedThreadPool();
            while (SERVER_UP) {
                CompletableFuture.runAsync(new ClientSession(serverSocket.accept()), exec);
            }
        } catch (IOException ex) {
            System.out.println(String.format("Server could not listen on port %s. " +
                    "Please run the server with another port.", PORT));
            ex.printStackTrace();
            System.exit(-1);
        }
    }

}
