package com.atom.matchmaker.services;

import com.atom.matchmaker.controllers.GameServiceController;
import com.atom.matchmaker.models.Session;
import com.atom.matchmaker.repositories.PlayersRepository;
import com.atom.matchmaker.repositories.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GameService {


    private static final Logger log = LoggerFactory.getLogger(GameServiceController.class);
    //private PlayersRepository playersRepository;
    private SessionRepository sessionRepository;

    @Autowired
    public GameService(PlayersRepository playersRepository, SessionRepository sessionRepository) {
        //this.playersRepository = playersRepository;
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(int playerCount)
    {
        log.info("Creating game with playerCount = {}", playerCount);
        Session session = new Session(playerCount);
        sessionRepository.save(session);
        return session;
    }
}
