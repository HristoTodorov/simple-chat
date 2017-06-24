package com.simplechat.perf.client.message.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

/**
 * Created by lampt on 6/24/2017.
 */
@FunctionalInterface
public interface MessageReciever {
    String messageRecieve() throws IOException;
}
