package com.atom.matchmaker.config;

import com.atom.matchmaker.network.Broker;
import com.atom.matchmaker.network.EventHandler;
import com.atom.matchmaker.repositories.PlayersQueue;
import com.atom.matchmaker.repositories.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.sql.rowset.BaseRowSet;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private PlayersRepository playersRepository;
    private Broker broker;

    @Autowired
    public WebSocketConfig(PlayersRepository playersRepository, Broker broker)
    {
        this.playersRepository = playersRepository;
        this.broker = broker;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new EventHandler(playersRepository, broker), "/events/connect")
                .setAllowedOrigins("*")
        //        .withSockJS()
        ;
    }

}