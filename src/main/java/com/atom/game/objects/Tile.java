package com.atom.game.objects;

import org.json.JSONException;
import org.json.JSONObject;
import com.atom.game.geometry.Point;

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
            position.put("x", (double) getPosition().getX() * 32);
            position.put("y", (double) getPosition().getY() * 32);
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
