package com.atom.matchmaker.controllers;

import com.atom.matchmaker.repositories.PlayersRepository;
import com.atom.matchmaker.repositories.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.atom.models.Player;
import com.atom.models.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/game")
public class GameServiceController {
    private static final Logger log = LoggerFactory.getLogger(GameServiceController.class);

    private PlayersRepository playersRepository;
    private SessionRepository sessionRepository;

    @Autowired
    public GameServiceController(PlayersRepository playersRepository, SessionRepository sessionRepository) {
        this.playersRepository = playersRepository;
        this.sessionRepository = sessionRepository;
    }

//    @RequestMapping(
//            path = "create",
//            method = RequestMethod.POST)
//    public ResponseEntity<String> create(@RequestParam("playerCount") int playerCount) {
//        log.info("Creating game with playerCount = {}", playerCount);
//        Session session = new Session(playerCount);
//        sessionRepository.save(session);
//        return new ResponseEntity<>(String.valueOf(session.getId()), HttpStatus.OK);
//    }

//    @RequestMapping(
//            path = "start",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<String> start(@RequestParam("gameId") long gameId) {
//        log.info("Staring game with gameId {}", gameId);
//        Optional<Session> optionalSession = sessionRepository.findById(gameId);
//        if (!optionalSession.isPresent())
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        // game starts
//        Session session = optionalSession.get();
//        return new ResponseEntity<>(String.valueOf(gameId), HttpStatus.OK);
//    }


    @RequestMapping(
            path = "connect",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> connect(@RequestParam("gameId") int gameId, @RequestParam("name") String name) {
        log.info("Connecting to gameId = {} with name = {}", gameId, name);
//        Session session = sessionRepository.get(gameId);
//        Player player = playersRepository.get(name);
//        if (session == null) return new ResponseEntity<>("No such session", HttpStatus.BAD_REQUEST);
//        if (player == null) return new ResponseEntity<>("No such player", HttpStatus.BAD_REQUEST);
//        session.addPlayer(player);
        // Connecting user to game
        return new ResponseEntity<>("will be implemented", HttpStatus.OK);
    }


//    @GetMapping(path = "leaderboard", produces=MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public HashMap<String, Long> getLeaderboard() {
//        log.info("Getting leaderboard...");
//        HashMap<String, Long> leaderboard = new HashMap<>();
//        List<Player> players = playersRepository.findAll();
//        for (Player player : players)
//        {
//            leaderboard.put(player.getUsername(), player.getSession().getId());
//        }
//        return leaderboard;
//    }
}