package com.simplechat.perf.client.message.impl;

import com.simplechat.perf.client.message.api.MessageSender;
import com.simplechat.perf.client.message.api.annotations.ClientSocket;

import javax.inject.Inject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by lampt on 6/24/2017.
 */
public class MessageSenderImpl implements MessageSender {

    private Socket clientSocket;

    @Inject
    public MessageSenderImpl(@ClientSocket Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void sendMessage(String message) throws IOException {
        System.out.println("Send message : " + message);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
        out.println(message);
        out.flush();
    }
}
