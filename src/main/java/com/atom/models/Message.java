package com.atom.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Message {
    private String topic;
    private Object data;
    private int playerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return playerId == message.playerId &&
                Objects.equals(topic, message.topic) &&
                Objects.equals(data, message.data);
    }

    @JsonCreator
    public Message(@JsonProperty("topic") String topic, @JsonProperty("data") Object data) {
        this.topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public Object getData() {
        return data;
    }
}
