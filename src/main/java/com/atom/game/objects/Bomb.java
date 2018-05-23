package com.atom.game.objects;

import org.json.JSONException;
import org.json.JSONObject;
import com.atom.game.geometry.Point;
import com.atom.game.tickables.Tickable;

public class Bomb extends GameObject implements Tickable {
    private int LIFETIME = 2000;
    private int elapsed = 0;
    private int owner;

    public Bomb(int id, Point position, int owner) {
        super(id, 28, 28, position);
        this.owner = owner;
    }

    public int getOwner() {
        return owner;
    }

    @Override
    public void tick(long elapsed) {
        this.elapsed += elapsed;
    }

    public boolean isAlive() {
        return elapsed < LIFETIME;
    }

    //{"position":{"x":16.0,"y":12.0},"id":16,"type":"Bomb"}
    @Override
    public String toString() {
        JSONObject position = new JSONObject();
        try {
            position.put("x", (double) getPosition().getX() * 32);
            position.put("y", (double) getPosition().getY() * 32);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject resultJson = new JSONObject();

        try {
            resultJson.put("position",position);
            resultJson.put("id", getId());
            resultJson.put("type", "Bomb");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJson.toString();
    }
}
