package com.simplechat.perf.client.configuration;

import com.google.inject.Inject;
import com.simplechat.perf.client.message.api.MessageComposer;
import com.simplechat.perf.client.message.api.MessageReciever;
import com.simplechat.perf.client.message.api.MessageSender;
import com.simplechat.perf.client.message.api.PerfClientStatistics;
import com.simplechat.shared.messages.Commands;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lampt on 6/18/2017.
 */
public class ClientPerf {

    private String clientName;

    private MessageSender mesageSender;

    private MessageReciever messageReciever;

    private MessageComposer messageComposer;

    private PerfClientStatistics stats;

    @Inject
    public ClientPerf(String clientName, MessageSender mesageSender, MessageReciever messageReciever,
                      MessageComposer messageComposer, PerfClientStatistics stats) {
        this.clientName = clientName;
        this.mesageSender = mesageSender;
        this.messageReciever = messageReciever;
        this.messageComposer = messageComposer;
        this.stats = stats;
    }

    public void run() {
        //first log the user
        final String userName = String.format(Commands.USER + " " + clientName);
        try {
            mesageSender.sendMessage(userName);
            messageReciever.messageRecieve();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotBlank(stats.getStatsFile())) {
            Runtime.getRuntime().addShutdownHook(new Thread(stats::makeReport));
        }

        // TODO make this constant for the available cores
        Executor exec = Executors.newFixedThreadPool(4);

        CompletableFuture<String> sendJob =
                CompletableFuture.supplyAsync(() -> {
                    String message = null;
                    while ((message = messageComposer.composeSendTypeMessage()) != null) {
                        try {
                            mesageSender.sendMessage(message);
                            stats.logMessageSend(message);
                            // send list Command
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
                            stats.logMessageRecieved(messageReciever.messageRecieve());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, exec);
        sendJob.thenCombine(recieveJob, (x, y) -> x.toLowerCase());
    }

}
