package com.simplechat.perf.client.message.api;

/**
 * Created by lampt on 6/24/2017.
 */
public interface PerfClientStatistics {
    void logMessageSend(String message);

    void logMessageRecieved(String message);

    void makeReport();

    String getStatsFile();
}
