package com.simplechat.perf.client.message.impl;

import com.simplechat.perf.client.message.api.PerfClientStatistics;
import com.simplechat.perf.client.message.api.annotations.ClientName;
import com.simplechat.perf.client.message.api.annotations.StatsFile;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lampt on 6/18/2017.
 */
public class PerfClientStatisticsImpl implements PerfClientStatistics {

    private final List<String> sendMessages = new ArrayList<>();

    private final List<String> recievedMessages = new ArrayList<>();

    private final String clientName;

    private String statsFile;

    public String getStatsFile() {
        return statsFile;
    }

    @Inject
    public PerfClientStatisticsImpl(@ClientName String clientName, @StatsFile String statsFile) {
        this.clientName = clientName;
        this.statsFile = statsFile;
    }

    @Override
    public void logMessageSend(String message) {
        sendMessages.add(LocalDateTime.now() + " SEND MESSAGE - " + message);
    }

    @Override
    public void logMessageRecieved(String message) {
        recievedMessages.add(LocalDateTime.now() + " RECIEVED MESSAGE - " + message);
    }

    @Override
    public void makeReport() {
        //TODO implement
    }
}
