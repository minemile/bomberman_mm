package com.atom.models;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@ToString(exclude = {"players"})
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int playerCount;

    @OneToMany
    private List<Player> players = new ArrayList<>();

    private boolean isFinished = false;
    private double rating;

    public Session() {}

    public Session(int playerCount) {this.playerCount = playerCount;}

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
