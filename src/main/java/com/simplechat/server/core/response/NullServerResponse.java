package com.simplechat.server.core.response;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lampt on 6/17/2017.
 */
public class NullServerResponse implements IServerResponse {

    private final ResponseCode code = ResponseCode.ERROR;

    @NotNull
    @Override
    public String toString() {
        String message = "server error";
        return code + "" + message;
    }
}
