package com.atom.matchmaker.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int playerCount;

    @OneToMany(mappedBy = "session")
    private List<Player> players;

    private boolean isFinished = false;

    public Session() {}

    public Session(int playerCount) {
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
