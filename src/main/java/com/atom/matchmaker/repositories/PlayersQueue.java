package com.atom.matchmaker.repositories;

import com.atom.matchmaker.models.Player;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
@Data
public class PlayersQueue {
    BlockingQueue<Player> players = new LinkedBlockingQueue<>();
}
