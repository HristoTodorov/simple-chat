package com.simplechat.perf.client.message.api;

/**
 * Created by lampt on 6/24/2017.
 */
public interface MessageResolution {
    long getUnicastMessageNumber();

    long getBroadcastMessageNumber();

    long getListMessageNumber();
}
