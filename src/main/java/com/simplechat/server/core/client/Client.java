package com.simplechat.server.core.client;

import java.net.SocketAddress;

/**
 * Created by lampt on 6/17/2017.
 */
public interface Client {
    String getUserName();

    SocketAddress getAddress();

    boolean isRegistered();

    void setRegistered(boolean isRegistered);
}
