package com.simplechat.perf.client;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lampt on 6/18/2017.
 */
class PerfClientStatistics {

    private final List<String> sendMessages = new ArrayList<>();

    private final List<String> recievedMessages = new ArrayList<>();

    private final String clientName;

    private String statsFile;

    public PerfClientStatistics(String clientName, String statsFile) {
        this(clientName);
        this.statsFile = statsFile;
    }

    private PerfClientStatistics(String clientName) {
        this.clientName = clientName;
    }

    public void onMessageSend(String message) {
        sendMessages.add(LocalDateTime.now() + " SEND MESSAGE - " + message);
    }

    public void onMessageRecieved(String message) {
        recievedMessages.add(LocalDateTime.now() + " RECIEVED MESSAGE - " + message);
    }

    public void makeReport() {
        //TODO implement
    }
}
