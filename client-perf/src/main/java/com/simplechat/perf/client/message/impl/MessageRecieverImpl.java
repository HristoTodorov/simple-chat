package com.simplechat.perf.client.message.impl;

import com.simplechat.perf.client.message.api.MessageReciever;
import com.simplechat.perf.client.message.api.annotations.ClientSocket;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * Created by lampt on 6/24/2017.
 */
public class MessageRecieverImpl implements MessageReciever {

    private Socket clientSocket;

    @Inject
    public MessageRecieverImpl(@ClientSocket Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public String messageRecieve() throws IOException {
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
        String message = inFromServer.readLine();
        System.out.println(LocalDateTime.now() + " Recieved message: " + message);
        return message;
    }
}
