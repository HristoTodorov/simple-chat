package com.simplechat.perf.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lampt on 6/18/2017.
 */
class ClientPerf {
    private Socket clientSocket;
    private int port = 5555;
    private final long clientSuffix;
    private final long numberOfUnicastMessages;
    private final long numberOfBroadcastMessages;
    private final long numberOfListMessages;
    private final String statsFile;

    // TODO too musch arguments - think for builder pattern
    public ClientPerf(int port, long clientSuffix, long numberOfUnicastMessages,
                      long numberOfBroadcastMessages, long numberOfListMessages,
                        String statsFile) {
        this.port = port;
        this.clientSuffix = clientSuffix;
        this.numberOfUnicastMessages = numberOfUnicastMessages;
        this.numberOfBroadcastMessages = numberOfBroadcastMessages;
        this.numberOfListMessages = numberOfListMessages;
        this.statsFile = statsFile;
        initPerfClient();
    }

    private class MessageResolution {
        private long unicastMessageNumber;

        private long broadcastMessageNumber;

        private long listMessageNumber;

        public MessageResolution(long unicastMessageNumber, long broadcastMessageNumber, long listMessageNumber) {
            this.unicastMessageNumber = unicastMessageNumber;
            this.broadcastMessageNumber = broadcastMessageNumber;
            this.listMessageNumber = listMessageNumber;
        }

        public long getUnicastMessageNumber() {
            return unicastMessageNumber--;
        }

        public long getBroadcastMessageNumber() {
            return broadcastMessageNumber--;
        }

        public long getListMessageNumber() {
            return listMessageNumber--;
        }
    }

    private class MessageComposer {
        private final MessageResolution messageResolution;

        public MessageComposer(MessageResolution resolution) {
            this.messageResolution = resolution;
        }

        public String composeSendTypeMessage() {
            Random rand = new SecureRandom();
            //TODO send list command
            long clientId;
            if ((clientId = rand.longs().limit(numberOfUnicastMessages).filter(
                    (x) -> x % 2 == 0).distinct().count()) % 2 != 0) {
                if (messageResolution.getUnicastMessageNumber() >= 0) {
                    return String.format("send_to acc%s private message !!!!", clientId);
                } else if (messageResolution.getBroadcastMessageNumber() >= 0) {
                    return "send_all some very very handsome message !&%$#$ $@%@#$%$@# >R>TER?DFSD>fsd";
                } else {
                    return null;
                }
            } else {
                if (messageResolution.getBroadcastMessageNumber() >= 0) {
                    return "send_all nobody nobody can drag me down ....!!";
                } else if (messageResolution.getUnicastMessageNumber() >= 0) {
                    return String.format("send_to acc%s private message !!!!", clientId);
                } else {
                    return null;
                }
            }
        }

        public String composeListTypeMessage() {
            return null;// TODO implement
        }

        public String composeSendFileTypeMessage() {
            return null; // TODO implement
        }
    }

    private void initPerfClient() {
        try {
            clientSocket = new Socket("localhost", port);
        } catch (IOException socketException) {
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    public void run() {
        //first log the user
        final String userName = String.format("user acc%d", clientSuffix);
        try {
            sendMessage(userName);
            recieveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PerfClientStatistics stats = new PerfClientStatistics(userName, statsFile);

        if (statsFile != null) {
            Runtime.getRuntime().addShutdownHook(new Thread(stats::makeReport));
        }

        Executor exec = Executors.newFixedThreadPool(4);

        CompletableFuture<String> sendJob =
                CompletableFuture.supplyAsync(() -> {
                    MessageComposer messageComposer = new MessageComposer(
                            new MessageResolution(numberOfUnicastMessages,
                                    numberOfBroadcastMessages, numberOfListMessages));
                    String message = null;
                    while ((message = messageComposer.composeSendTypeMessage()) != null) {
                        try {
                            sendMessage(message);
                            stats.onMessageSend(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return ""; // FIXME
                }, exec);

        CompletableFuture<String> recieveJob =
                CompletableFuture.supplyAsync(() -> {
                    while (true) {
                        try {
                            stats.onMessageRecieved(recieveMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, exec);
        sendJob.thenCombine(recieveJob, (x, y) -> x.toLowerCase());
    }

    private void sendMessage(String message) throws IOException {
        System.out.println("Send message : " + message);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        out.println(message);
        out.flush();
    }

    private String recieveMessage() throws IOException {
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
        String message = inFromServer.readLine();
        System.out.println(LocalDateTime.now() + " Recieved message: " + message);
        return message;
    }
}
