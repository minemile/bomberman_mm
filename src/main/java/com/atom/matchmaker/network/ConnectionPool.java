package com.atom.matchmaker.network;

import com.atom.models.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConnectionPool {
    private static final Logger log = LogManager.getLogger(ConnectionPool.class);
    private static final ConnectionPool instance = new ConnectionPool();
    private static final int PARALLELISM_LEVEL = 4;

    private final ConcurrentHashMap<WebSocketSession, Player> pool;

    public static ConnectionPool getInstance() {
        return instance;
    }

    private ConnectionPool() {
        pool = new ConcurrentHashMap<>();
    }

    public void send(@NotNull WebSocketSession session, @NotNull String msg) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(msg));
            } catch (IOException ignored) {
            }
        }
    }

    public void broadcast(@NotNull String msg) {
        pool.forEachKey(PARALLELISM_LEVEL, session -> send(session, msg));
    }

    public void shutdown() {
        pool.forEachKey(PARALLELISM_LEVEL, session -> {
            if (session.isOpen()) {
                try {
                    session.close();
                } catch (IOException ignored) {
                }
            }
        });
    }

    public Player getPlayer(WebSocketSession session) {
        return pool.get(session);
    }

    public WebSocketSession getSession(Player player) {
        WebSocketSession webSocketSession = null;
        while(webSocketSession == null) {
            for (Map.Entry<WebSocketSession, Player> entry : pool.entrySet()) {
                if (entry.getValue().getUsername().equals(player.getUsername())) {
                    webSocketSession = entry.getKey();
                }
            }
        }
        return webSocketSession;
    }

    public void add(WebSocketSession session, Player player) {
        if (pool.putIfAbsent(session, player) == null) {
            log.info("{} joined", player);
        }
    }

    public void remove(WebSocketSession session) {
        pool.remove(session);
    }
}