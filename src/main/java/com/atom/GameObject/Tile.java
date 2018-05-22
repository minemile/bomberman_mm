package com.atom.GameObject;

import org.json.JSONException;
import org.json.JSONObject;
import com.atom.geometry.Point;

import java.util.HashMap;
import java.util.Map;

public class Tile extends GameObject {
    TileType type;

    public Tile(int id, TileType type, Point position) {
        super(id, 32, 32, position);
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    //{"position":{"x":16.0,"y":12.0},"id":16,"type":"Wall"}
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
            if (type == TileType.WALL) {
                resultJson.put("type", "Wall");
            } else {
                resultJson.put("type", "Wood");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJson.toString();
    }
}
