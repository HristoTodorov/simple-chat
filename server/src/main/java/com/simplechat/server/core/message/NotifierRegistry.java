package com.simplechat.server.core.message;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lampt on 6/17/2017.
 */
public final class NotifierRegistry {

    private static final Map<String, AbstractMessageNotifier> NOTIFIERS = new ConcurrentHashMap<>();

    private NotifierRegistry() {
    }

    public static void registerNotifier(String userName, AbstractMessageNotifier notifier) {
        NOTIFIERS.putIfAbsent(userName, notifier);
    }

    public static void removeNotifier(String userName) {
        NOTIFIERS.remove(userName);
    }

    public static AbstractMessageNotifier getNotifier(String userName) {
        return NOTIFIERS.getOrDefault(userName, new NullMessageNotifier());
    }

    public static Collection<AbstractMessageNotifier> getNotifiers() {
        return NOTIFIERS.values();
    }
}
