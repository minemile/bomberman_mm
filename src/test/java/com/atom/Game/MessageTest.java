package com.atom.Game;

import org.assertj.core.error.ShouldBeAfterYear;
import org.junit.Test;
import com.atom.models.DataTypes.DataDirection;
import com.atom.models.Message;
import com.atom.models.Player;
import com.atom.models.GameSession;
import com.atom.utils.JsonHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static com.atom.utils.JsonHelper.fromJson;
import static com.atom.utils.JsonHelper.toJson;

public class MessageTest {
    @Test
    public void testBombMessageFromJson()
    {
        Message myMessage = new Message("BOMB", "");
        String JsonString = toJson(myMessage);

        Message result = fromJson(JsonString, Message.class);
        assertEquals(result, myMessage);
    }

    @Test
    public void testMoveMessageFromJson()
    {
        Message myMessage = new Message("MOVE", "UP");
        String JsonString = toJson(myMessage);

        Message result = fromJson(JsonString, Message.class);
        assertEquals(myMessage, result);
    }
}