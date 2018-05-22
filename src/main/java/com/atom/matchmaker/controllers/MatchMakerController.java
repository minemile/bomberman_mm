package com.atom.matchmaker.controllers;

import com.atom.models.Player;
import com.atom.matchmaker.repositories.PlayersQueue;
import com.atom.matchmaker.repositories.PlayersRepository;
import com.atom.matchmaker.repositories.SessionRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.Optional;


@Controller
@RequestMapping("/matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    private static final int PLAYERS_COUNT = 4;

    private PlayersRepository playersRepository;
    private SessionRepository sessionRepository;
    private PlayersQueue playersQueue;

    @Autowired
    public MatchMakerController(PlayersRepository playersRepository, SessionRepository sessionRepository,
                                PlayersQueue playersQueue) {
        this.playersRepository = playersRepository;
        this.sessionRepository = sessionRepository;
        this.playersQueue = playersQueue;
    }

    @RequestMapping(
            path = "join",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> join(@RequestParam("name") String name) throws InterruptedException {
        log.info("Joining to game with name = {}", name);
        Optional<Player> optionalPlayer = playersRepository.findByUsername(name);
        Player player;
        if (!optionalPlayer.isPresent()) {
            log.info("Player not found. Creating!");
            player = new Player(name, 0);
            playersRepository.save(player);
        } else {
            player = optionalPlayer.get();
        }
        playersQueue.getPlayers().offer(player);

        // Todo: this should be refactored. Get id after session creator found session.
        while(playersQueue.getPlayers().contains(player))
        {
            Thread.sleep(10);
        }
        Thread.sleep(10000);
        Player player1 = playersRepository.findById(player.getId()).get();
        log.info("Player session id is {}", player1.getSession().getId());
        return new ResponseEntity<>(String.valueOf(player1.getSession().getId()), HttpStatus.OK);
    }

    //TODO
    //сюда надо передать число игроков в игре, обратно возвращается gameID, далее в игру с этим ID должны добавиться игроки
    //возможно(и скорее всего) это надо делать в методе join
    private long create() throws IOException {
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "playerCount=" + PLAYERS_COUNT))
                .url(PROTOCOL + HOST + PORT + "/game/create")
                .build();
        Response response = client.newCall(request).execute();
        return Long.valueOf(response.body().string());
    }

    private void connect(int gameId, String name) throws IOException {
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType,
                        "gameId=" + gameId + "&name=" + name))
                .url(PROTOCOL + HOST + PORT + "/game/connect")
                .build();
        client.newCall(request).execute();
    }

    private void start(long gameId) throws IOException {
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, "gameId=" + gameId))
                .url(PROTOCOL + HOST + PORT + "/game/start")
                .build();
        client.newCall(request).execute();
    }
}
