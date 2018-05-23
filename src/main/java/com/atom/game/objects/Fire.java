package com.atom.game.objects;

import org.json.JSONException;
import org.json.JSONObject;
import com.atom.game.geometry.Point;
import com.atom.game.tickables.Tickable;

public class Fire extends GameObject implements Tickable {
    private int LIFETIME = 200;
    private int elapsed = 0;

    public Fire(int id, Point position) {
        super(id, 38, 38, position);
    }

    @Override
    public void tick(long elapsed) {
        this.elapsed += elapsed;
    }

    public boolean isAlive() {
        return elapsed < LIFETIME;
    }

    //{"position":{"x":16.0,"y":12.0},"id":16,"type":"Fire"}
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
            resultJson.put("type", "Fire");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJson.toString();
    }
}
