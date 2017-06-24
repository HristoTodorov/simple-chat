package com.simplechat.perf.client.message.impl;

import com.simplechat.perf.client.message.api.MessageResolution;
import com.simplechat.perf.client.message.api.annotations.BroadcastMessageNumber;
import com.simplechat.perf.client.message.api.annotations.ListMessageNumber;
import com.simplechat.perf.client.message.api.annotations.UnicastMessageNumber;

import javax.inject.Inject;

/**
 * Created by lampt on 6/24/2017.
 */
public class MessageResolutionImpl implements MessageResolution {

    private long unicastMessageNumber;

    private long broadcastMessageNumber;

    private long listMessageNumber;

    @Inject
    public MessageResolutionImpl(@UnicastMessageNumber long unicastMessageNumber,
                                 @BroadcastMessageNumber long broadcastMessageNumber,
                                 @ListMessageNumber long listMessageNumber) {
        this.unicastMessageNumber = unicastMessageNumber;
        this.broadcastMessageNumber = broadcastMessageNumber;
        this.listMessageNumber = listMessageNumber;
    }

    @Override
    public long getUnicastMessageNumber() {
        return unicastMessageNumber--;
    }

    @Override
    public long getBroadcastMessageNumber() {
        return broadcastMessageNumber--;
    }

    @Override
    public long getListMessageNumber() {
        return listMessageNumber--;
    }
}
