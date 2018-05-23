package com.atom.matchmaker.services;

import com.atom.matchmaker.controllers.GameServiceController;
import com.atom.matchmaker.network.Broker;
import com.atom.matchmaker.repositories.GameRepository;
import com.atom.matchmaker.repositories.SessionJPARepository;
import com.atom.models.GameSession;
import com.atom.models.Session;
import com.atom.matchmaker.repositories.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GameService {


    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    //private PlayersRepository playersRepository;
    private SessionRepository sessionRepository;
    private GameRepository gameRepository;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private TaskExecutor taskExecutor;
    private SessionJPARepository sessionJPARepository;
    private Broker broker;

    @Autowired
    public GameService(TaskExecutor taskExecutor, SessionJPARepository sessionJPARepository,
                       SessionRepository sessionRepository, GameRepository gameRepository, Broker broker) {
        this.sessionRepository = sessionRepository;
        this.taskExecutor = taskExecutor;
        this.gameRepository = gameRepository;
        this.sessionJPARepository = sessionJPARepository;
        this.broker = broker;
    }

    public Session createSession(int playerCount)
    {
        log.info("Creating game with playerCount = {}", playerCount);
        //int sessionId = atomicInteger.getAndIncrement();
        Session session = new Session(playerCount);
        sessionJPARepository.save(session);
        sessionRepository.getSessions().put(session.getId(), session);
        return session;
    }

    public void startSession(int gameId)
    {
        log.info("Staring session {}", gameId);
        Session session = sessionRepository.getSessions().get(gameId);
        GameSession gameSession = new GameSession(session.getId(), session.getPlayers(), broker);
        gameRepository.getGames().put(session.getId(), gameSession);
        taskExecutor.execute(gameSession);
    }
}
