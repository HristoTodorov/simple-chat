package com.simplechat.perf.client.configuration;

import com.google.inject.AbstractModule;
import com.simplechat.perf.client.message.api.*;
import com.simplechat.perf.client.message.api.annotations.*;
import com.simplechat.perf.client.message.impl.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by lampt on 6/24/2017.
 */
public class ClientPerfModule extends AbstractModule {

    private long unicastMsgNumber;

    private long listMsgNumber;

    private long broadcastMsgNumber;

    private Socket clientSocket;

    private String clientName;

    private String statsFile;

    public ClientPerfModule(long unicastMsgNumber, long listMsgNumber, long broadcastMsgNumber,
                            int port, String clientName, String statsFile) {
        this.unicastMsgNumber = unicastMsgNumber;
        this.listMsgNumber = listMsgNumber;
        this.broadcastMsgNumber = broadcastMsgNumber;
        this.clientName = clientName;
        this.statsFile = StringUtils.isBlank(statsFile) ? "" : statsFile;
        try {
            clientSocket = new Socket("localhost", port);
        } catch (IOException socketException) {
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(BroadcastMessageNumber.class).to(broadcastMsgNumber);
        bindConstant().annotatedWith(UnicastMessageNumber.class).to(unicastMsgNumber);
        bindConstant().annotatedWith(ListMessageNumber.class).to(listMsgNumber);
        bindConstant().annotatedWith(ClientName.class).to(clientName);
        bindConstant().annotatedWith(StatsFile.class).to(statsFile);
        bind(MessageResolution.class).to(MessageResolutionImpl.class);
        bind(MessageComposer.class).to(MessageComposerImpl.class);
        bind(MessageReciever.class).to(MessageRecieverImpl.class);
        bind(MessageSender.class).to(MessageSenderImpl.class);
        bind(PerfClientStatistics.class).to(PerfClientStatisticsImpl.class);
        bind(Socket.class).annotatedWith(ClientSocket.class).toInstance(clientSocket);
        bind(ClientPerf.class);
    }
}
