package com.atom.GameObject;

import org.json.JSONException;
import org.json.JSONObject;
import com.atom.geometry.Point;

import com.atom.tickables.Tickable;
import java.util.HashMap;
import java.util.Map;


import static com.atom.GameObject.PawnState.IDLE;

public class Pawn extends GameObject implements Tickable {
    private int BASIC_SPEED = 10;
    private int SPEED_PERK = 5;
    private int speedBonus = 0;
    private int bombBonus = 1;
    private int forceBonus = 1;
    private PawnState state = IDLE;
    private Point pix_position;

    public Pawn(int id, Point position) {
        super(id, 26, 26, position);
        this.pix_position = new Point(position.getX() * 32 + 16, position.getY()*32 + 16);
    }

    public int getBombBonus() {
        return bombBonus;
    }

    public Point getPix_position() {
        return pix_position;
    }

    public Point getSq_position() {
        return getPosition();
    }

    public void incSpeed() {
        speedBonus++;
    }

    public void incBomb() {
        bombBonus++;
    }

    public void incForce() {
        forceBonus++;
    }

    public int getForceBonus() {
        return forceBonus;
    }

    public void setState(PawnState state) {
        this.state = state;
    }

    public int getSpeed() {
        if (state == IDLE) {
            return 0;
        }
        return BASIC_SPEED + SPEED_PERK * speedBonus;
    }

    public void move(int x, int y) {
        pix_position.setX(pix_position.getX() + x);
        pix_position.setY(pix_position.getY() + y);
        setPosition(new Point(pix_position.getX() / 32, pix_position.getY() / 32));
    }

    @Override
    public void tick(long elapsed) {
        move((int)elapsed * getSpeed(), (int)elapsed * getSpeed());
    }

    //{"position":{"x":32.0,"y":32.0},
    // "id":213,
    // "velocity":0.05,
    // "maxBombs":1,
    // "bombPower":1,
    // "speedModifier":1.0,
    // "type":"Pawn"}

    @Override
    public String toString() {
        JSONObject position = new JSONObject();
        try {
            position.put("x", (double) getPosition().getX());
            position.put("y", (double) getPosition().getY());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject resultJson = new JSONObject();

        try {
            resultJson.put("position",position);
            resultJson.put("id", getId());
            resultJson.put("velocity", (double)getSpeed());
            resultJson.put("maxBombs",bombBonus);
            resultJson.put("maxBombs",bombBonus);
            resultJson.put("bombPower",forceBonus);
            resultJson.put("speedModifier",speedBonus);
            resultJson.put("type", "Pawn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJson.toString().replace("\\\"", "\"");
    }
}
