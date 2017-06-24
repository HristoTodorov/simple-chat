package com.simplechat.perf.client.message.impl;

import com.simplechat.perf.client.message.Message;
import com.simplechat.perf.client.message.api.PerfClientStatistics;
import com.simplechat.perf.client.message.api.annotations.ClientName;
import com.simplechat.perf.client.message.api.annotations.StatsFile;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lampt on 6/18/2017.
 */
public class PerfClientStatisticsImpl implements PerfClientStatistics {

    private final List<Message> sendMessages = new ArrayList<>();

    private final List<Message> recievedMessages = new ArrayList<>();

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
        Message msg = new Message(LocalDateTime.now(), "SEND MESSAGE - " + message);
        sendMessages.add(msg);
    }

    @Override
    public void logMessageRecieved(String message) {
        Message msg = new Message(LocalDateTime.now(), "RECIEVED MESSAGE - " + message);
        recievedMessages.add(msg);
    }

    @Override
    public void makeReport() {
        try {
            File reportFile = Paths.get(statsFile, clientName).toFile();
            FileUtils.touch(reportFile);
            String sendedMessagesStringifier = stringify(sendMessages);
            FileUtils.write(reportFile, sendedMessagesStringifier, Charset.forName("UTF-8"));
            String recievedMessagesStringifier = stringify(recievedMessages);
            FileUtils.write(reportFile, recievedMessagesStringifier, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO implement
    }

    private String stringify(List<Message> messages) {
        return messages.stream().sorted((that, other )-> that.getDate().compareTo(other.getDate())).
                map(message -> message.toString()).collect(Collectors.joining("\n"));
    }
}
