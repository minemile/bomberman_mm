package com.atom.matchmaker.services;

import com.atom.matchmaker.controllers.GameServiceController;
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


    private static final Logger log = LoggerFactory.getLogger(GameServiceController.class);
    //private PlayersRepository playersRepository;
    private SessionRepository sessionRepository;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private TaskExecutor taskExecutor;

    @Autowired
    public GameService(TaskExecutor taskExecutor, SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        this.taskExecutor = taskExecutor;
    }

    public Session createSession(int playerCount)
    {
        log.info("Creating game with playerCount = {}", playerCount);
        int sessionId = atomicInteger.getAndIncrement();
        Session session = new Session(sessionId, playerCount);
        sessionRepository.getSessions().put(sessionId, session);
        return session;
    }

    public void startSession(int gameId)
    {
        log.info("Staring session {}", gameId);
        Session session = sessionRepository.getSessions().get(gameId);

        //taskExecutor.execute(session);
    }
}
