package com.atom.matchmaker.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class GameSession {

    private long id;
    private int playerCount;

    private List<Player> players = new ArrayList<>();

    private boolean isFinished = false;
    private double rating;

    public GameSession() {}

    public GameSession(int playerCount) {
        this.playerCount = playerCount;
    }


    public boolean isFull() {
        return players.size() >= playerCount;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public double getAverageRating() {
        double rating = 0;
        for (Player player : players)
            rating += player.getRating();
        return rating / players.size();
    }
}
