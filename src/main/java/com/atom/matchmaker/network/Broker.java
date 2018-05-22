package com.atom.matchmaker.network;

import com.atom.matchmaker.models.Message;
import com.atom.matchmaker.models.Player;
import com.atom.matchmaker.models.Session;
import com.atom.matchmaker.models.Topic;
import com.atom.matchmaker.repositories.SessionRepository;
import com.atom.matchmaker.utils.JsonHelper;
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

        Message message = JsonHelper.fromJson(msg, Message.class);
        Player player = connectionPool.getPlayer(session);
        int sessionId = player.getSession().getId();
        Session session1 = sessionRepository.getSessions().get(sessionId);
        //session1.getQueue().offer(msg);
        //TODO TASK2 implement message processing
    }

    public void send(@NotNull Player player, @NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        WebSocketSession session = connectionPool.getSession(player);
        connectionPool.send(session, message);
    }

    public void broadcast(@NotNull Topic topic, @NotNull Object object) {
        String message = JsonHelper.toJson(new Message(topic, JsonHelper.toJson(object)));
        connectionPool.broadcast(message);
    }

}