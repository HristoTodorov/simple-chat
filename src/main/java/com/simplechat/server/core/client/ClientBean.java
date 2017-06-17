package com.simplechat.server.core.client;

import com.google.common.base.Objects;

import java.net.SocketAddress;

/**
 * Created by lampt on 6/17/2017.
 */
public class ClientBean implements Client {

    private final String userName;

    private final SocketAddress socketAddress;

    private boolean isRegistered;

    public ClientBean(String userName, SocketAddress socketAddress) {
        this.userName = userName;
        this.socketAddress = socketAddress;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public SocketAddress getAddress() {
        return socketAddress;
    }

    @Override
    public boolean isRegistered() {
        return isRegistered;
    }

    @Override
    public void setRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientBean)) return false;
        ClientBean that = (ClientBean) o;
        return Objects.equal(getUserName(), that.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserName());
    }
}
