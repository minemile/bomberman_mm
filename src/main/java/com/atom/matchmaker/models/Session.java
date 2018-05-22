package com.atom.matchmaker.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Data
@ToString(exclude = {"players"})
public class Session {

    private int id;
    private int playerCount;

    private List<Player> players = new ArrayList<>();

    private boolean isFinished = false;
    private double rating;

    public Session(int id, int playerCount) {this.id = id; this.playerCount = playerCount;}

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
