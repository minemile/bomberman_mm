package com.atom.matchmaker.network;

import com.atom.matchmaker.controllers.GameServiceController;
import com.atom.matchmaker.models.Player;
import com.atom.matchmaker.repositories.PlayersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Component
public class EventHandler extends TextWebSocketHandler implements WebSocketHandler {
    private static ConnectionPool connectionPool = ConnectionPool.getInstance();
    private PlayersRepository playersRepository;
    private static final Logger log = LoggerFactory.getLogger(EventHandler.class);

    public EventHandler(PlayersRepository playersRepository)
    {
        this.playersRepository = playersRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String name = session.getUri().getQuery().split("=")[2];
        Optional<Player> playerOptional = playersRepository.findByUsername(name);
        if (playerOptional.isPresent())
        {
            Player player = playerOptional.get();
            log.info("Got player {}", player);
            connectionPool.add(session, player);
        } else {
            log.error("Could not file player with name {}", name);
        }
        //String playerName = session.getUri().getQuery().split("")
        log.info("Socket Connected: " + session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //session.sendMessage(new TextMessage("{ \"history\": [ \"ololo\", \"2\" ] }"));
        Broker.getInstance().receive(session, message.getPayload());
        log.info("Received " + message.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Socket Closed: [" + closeStatus.getCode() + "] " + closeStatus.getReason());
        super.afterConnectionClosed(session, closeStatus);
    }

}
