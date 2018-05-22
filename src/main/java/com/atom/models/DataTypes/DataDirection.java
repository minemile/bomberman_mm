package com.atom.models.DataTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataDirection {
    String direction;

    @JsonCreator
    public DataDirection(@JsonProperty("direction") String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }
}
