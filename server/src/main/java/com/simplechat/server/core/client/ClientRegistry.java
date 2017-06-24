package com.simplechat.server.core.client;

import com.google.common.collect.Sets;
import com.simplechat.server.core.configuration.Server;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Created by lampt on 6/17/2017.
 */
public final class ClientRegistry {

    private static final Set<Client> REGISTRY = Sets.newConcurrentHashSet();

    private ClientRegistry() {
    }

    public static void registerClient(@NotNull Client client) {
        if (REGISTRY.add(client)) {
            if (!Server.isSilentModeOn()) {
                System.out.println("Client");
            }
            client.setRegistered(true);
        }
    }

    @NotNull
    public static Set<Client> getConnectedClients() {
        return REGISTRY;
    }

    public static void unregisterClient(SocketAddress socketAddress) {
        Optional<Client> removedClient = REGISTRY.stream()
                .filter(client -> client.getAddress().equals(socketAddress))
                .findFirst();
        removedClient.ifPresent(REGISTRY::remove);
    }

    public static void unregisterClient(final String userName) {
        Optional<Client> removedClient = REGISTRY.stream()
                .filter(client -> client.getUserName().equals(userName))
                .findFirst();
        removedClient.ifPresent(REGISTRY::remove);
    }

    public static Optional<Client> getClient(final String userName) {
        Objects.requireNonNull(userName);
        return REGISTRY.stream().filter(client -> client.getUserName().equals(userName)).findFirst();
    }
}
