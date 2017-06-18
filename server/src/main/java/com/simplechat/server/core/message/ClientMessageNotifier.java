package com.simplechat.server.core.message;

import java.io.PrintWriter;

/**
 * Created by lampt on 6/17/2017.
 */
public class ClientMessageNotifier implements AbstractMessageNotifier {

    private final PrintWriter writer;

    public ClientMessageNotifier(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void onMessage(String message) {
        writer.print(message);
        writer.flush();
    }
}
