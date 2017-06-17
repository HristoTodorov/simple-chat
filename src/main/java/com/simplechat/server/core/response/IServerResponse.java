package com.simplechat.server.core.response;

/**
 * Created by lampt on 6/17/2017.
 */
public interface IServerResponse {

    enum ResponseCode {
        OK(200), ERROR(500), RECIEVED(300), USER_GONE(400);

        final Integer statusCode;

        ResponseCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase() + " " + statusCode;
        }
    }
}
