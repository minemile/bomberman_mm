package com.atom.matchmaker.network;

import com.atom.models.Message;
import com.atom.models.Player;
import com.atom.models.Session;
import com.atom.matchmaker.repositories.SessionRepository;
import com.atom.utils.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;

@Component
public class Broker {
    private static final Logger log = LogManager.getLogger(Broker.class);

    private static final Broker instance = new Broker();
    private final ConnectionPool connectionPool;
    private SessionRepository sessionRepository;

    public static Broker getInstance() {
        return instance;
    }

    private Broker() {
        this.connectionPool = ConnectionPool.getInstance();
    }

    public void receive(@NotNull WebSocketSession session, @NotNull String msg) {
        log.info("RECEIVED: " + msg);
        Player player = connectionPool.getPlayer(session);
        int sessionId = player.getSession().getId();
        Session gameSession = sessionRepository.getSessions().get(sessionId);
        gameSession.getQueue().offer(msg);
    }

    public void send(@NotNull Player player, @NotNull String message) {
        //String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        WebSocketSession session = connectionPool.getSession(player);
        connectionPool.send(session, message);
    }

    public void broadcast(@NotNull String message) {
        //String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        connectionPool.broadcast(message);
    }

}