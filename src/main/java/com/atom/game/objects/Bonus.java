package com.atom.game.objects;

import org.json.JSONException;
import org.json.JSONObject;
import com.atom.game.geometry.Point;

public class Bonus extends GameObject {
    BonusType type;

    public Bonus(int id, BonusType type, Point position) {
        super(id, 30, 30, position);
        this.type = type;
    }

    public BonusType getType() {
        return type;
    }

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
            if (type == BonusType.SPEED) {
                resultJson.put("type", "SPEED");
            }
            if (type == BonusType.RANGE) {
                resultJson.put("type", "RANGE");
            }
            if (type == BonusType.BOMBS) {
                resultJson.put("type", "BOMBS");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //.toString().replace("\\\"", "\"")
        return resultJson.toString();
    }
}
