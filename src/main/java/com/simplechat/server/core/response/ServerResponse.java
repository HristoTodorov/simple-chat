package com.simplechat.server.core.response;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lampt on 6/17/2017.
 */
public class ServerResponse implements IServerResponse {

    private final ResponseCode code;

    private final String message;

    public ServerResponse(ResponseCode code, String message) {
        this.code = code;
        this.message = message;
    }

    @NotNull
    @Override
    public String toString() {
        return code + " " + message;
    }
}
