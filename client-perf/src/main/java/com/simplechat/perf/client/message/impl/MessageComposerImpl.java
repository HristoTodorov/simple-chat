package com.simplechat.perf.client.message.impl;

import com.simplechat.perf.client.message.api.MessageComposer;
import com.simplechat.perf.client.message.api.MessageResolution;
import com.simplechat.shared.messages.Commands;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by lampt on 6/24/2017.
 */
public class MessageComposerImpl implements MessageComposer {

    private MessageResolution messageResolution;

    @Inject
    public MessageComposerImpl(MessageResolution messageResolution) {
        this.messageResolution = messageResolution;
    }

    @Override
    public String composeSendTypeMessage() {
        Random rand = new SecureRandom();
        long clientId;
        if ((clientId = rand.longs().limit(10000).filter(
                (x) -> x % 2 == 0).distinct().count()) % 2 != 0) {
            if (messageResolution.getUnicastMessageNumber() >= 0) {
                return String.format("send_to acc%s private message !!!!", clientId);
            } else if (messageResolution.getBroadcastMessageNumber() >= 0) {
                return "send_all some very very handsome message !&%$#$ $@%@#$%$@# >R>TER?DFSD>fsd";
            } else {
                return null;
            }
        } else {
            if (messageResolution.getBroadcastMessageNumber() >= 0) {
                return "send_all nobody nobody can drag me down ....!!";
            } else if (messageResolution.getUnicastMessageNumber() >= 0) {
                return String.format("send_to acc%s private message !!!!", clientId);
            } else {
                return null;
            }
        }
    }

    @Override
    public String composeListTypeMessage() {
        return messageResolution.getListMessageNumber() >= 0 ? Commands.LIST : null;
    }

    @Override
    public String composeSendFileTypeMessage() {
        return null; // TODO implement
    }
}
