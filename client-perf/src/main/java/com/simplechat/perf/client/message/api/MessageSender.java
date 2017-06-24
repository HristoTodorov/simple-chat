package com.simplechat.perf.client.message.api;

import java.io.IOException;

/**
 * Created by lampt on 6/24/2017.
 */

@FunctionalInterface
public interface MessageSender {
    void sendMessage(String message) throws IOException;
}
