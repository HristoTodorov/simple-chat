package com.simplechat.server.core.message;

import com.simplechat.server.core.response.IServerResponse;

/**
 * Created by lampt on 6/17/2017.
 */
public final class MessageUtils {

    private MessageUtils() {
    }

    public static String prepareRecievedMessage(String userName, String message) {
        return IServerResponse.ResponseCode.RECIEVED.toString() +
                " msg_from " +
                userName +
                " " +
                message;
    }

    public static String prepareGoneMessage(String userName) {
        return IServerResponse.ResponseCode.USER_GONE + " " + userName;
    }
}
