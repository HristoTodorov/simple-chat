package com.simplechat.perf.client.message;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by lampt on 6/24/2017.
 */
public class Message {
    private LocalDateTime date;

    private String message;

    public Message(LocalDateTime date, String message) {
        this.date = date;
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "date=" + date +
                ", message='" + message + '\'' +
                '}';
    }
}
