package com.atom.models;

public class Player {
    private int rating;
    private final String username;

    public Player(String username) {
        this.username = username;
    }

    public Player(String username, int rating) {
        this.username = username;
        this.rating = rating;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }
}
