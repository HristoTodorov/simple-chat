package com.simplechat.server.core.message;

/**
 * Created by lampt on 6/17/2017.
 */
public interface AbstractMessageNotifier {
    void onMessage(String message);
}
