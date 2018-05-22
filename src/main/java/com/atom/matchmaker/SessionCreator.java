package com.atom.matchmaker;

import com.atom.models.Player;
import com.atom.models.Session;
import com.atom.matchmaker.repositories.PlayersQueue;
import com.atom.matchmaker.repositories.PlayersRepository;
import com.atom.matchmaker.repositories.SessionRepository;
import com.atom.matchmaker.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

@Component
public class SessionCreator implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SessionCreator.class);

    private PlayersQueue playersQueue;
    private SessionRepository sessionRepository;
    private GameService gameService;
    private PlayersRepository playersRepository;
    private final static int COUNT_PLAYERS = 2;


    @Autowired
    public SessionCreator(PlayersQueue playersQueue, SessionRepository sessionRepository,
                          GameService gameService, @NotNull TaskExecutor taskExecutor, PlayersRepository playersRepository) {
        this.playersQueue = playersQueue;
        this.playersRepository = playersRepository;
        this.sessionRepository = sessionRepository;
        this.gameService = gameService;
        taskExecutor.execute(this);
    }

    @Override
    @Transactional
    public void run() {
        log.info("Staring session creator thread {}", Thread.currentThread().getId());
        while (!Thread.currentThread().isInterrupted()) {
            Player player;
            try {
                player = playersQueue.getPlayers().poll(10_000, TimeUnit.SECONDS);
                if (player == null) {
                    log.info("Queue has no players");
                    continue;
                }
                log.info("Got player {} ", player);
                log.info("Tryin' to find valid session(with rating). It uses manhattan distance");
                double threshold = 0;
                boolean found_session = false;
                do {
                    for (Session session : sessionRepository.getSessions().values()) {
                        if (!session.isFull()) {
                            double rating = Math.abs(session.getAverageRating() - player.getRating());
                            if (rating <= threshold) {
                                log.info("Found valid session {}", session.getId());
                                session.addPlayer(player);
                                player.setSession(session);
                                playersRepository.save(player);
                                found_session = true;
                                if (session.isFull()) {
                                    log.info("Session is full. Start the game {}", session.getId());
                                    gameService.startSession(session.getId());
                                    //sessionRepository.save(session);
                                }
                            }
                        }
                    }
                    threshold++;
                } while (threshold != 10 && (!sessionRepository.getSessions().isEmpty()));
                if (!found_session) {
                    Session session = gameService.createSession(COUNT_PLAYERS);
                    player.setSession(session);
                    playersRepository.save(player);
                    session.getPlayers().add(player);
                }
            } catch (InterruptedException e) {
                log.info("Could not find player!");
            }
        }
    }
}