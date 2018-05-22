package com.atom.matchmaker.network;

import com.atom.matchmaker.repositories.GameRepository;
import com.atom.models.GameSession;
import com.atom.models.Message;
import com.atom.models.Player;
import com.atom.models.Session;
import com.atom.matchmaker.repositories.SessionRepository;
import com.atom.utils.JsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;

@Component
public class Broker {
    private static final Logger log = LogManager.getLogger(Broker.class);

    private final ConnectionPool connectionPool;

    private GameRepository gameRepository;

    @Autowired
    public Broker(GameRepository gameRepository) {
        this.connectionPool = ConnectionPool.getInstance();
        this.gameRepository = gameRepository;
    }

    public void receive(@NotNull WebSocketSession session, @NotNull String msg) {
        log.info("RECEIVED: " + msg);
        Player player = connectionPool.getPlayer(session);
        int sessionId = player.getSession().getId();
        GameSession gameSession = gameRepository.getGames().getOrDefault(sessionId, null);
        if (gameSession == null)
        {
            log.error("Game didn't started yet");
        }
        else {
            log.info("Got game {}", gameSession);
            gameSession.pushMessage(player, msg);
        }
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