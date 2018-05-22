package com.atom.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Session session;

    private int rating;
    private final String username;


    public Player() {
        this.username = "Unknown";
        this.rating = 0;
    }

    public Player(String username) {
        this.username = username;
        this.rating = 0;
    }

    public Player(String username, int rating) {
        this.username = username;
        this.rating = rating;
    }

    @NotNull
    public int getRating() {
        return rating;
    }
}
