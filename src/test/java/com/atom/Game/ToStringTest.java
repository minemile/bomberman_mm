package com.atom.Game;

import org.junit.Test;
import com.atom.GameObject.Bomb;
import com.atom.GameObject.Pawn;
import com.atom.GameObject.Tile;
import com.atom.geometry.Point;
import com.atom.GameObject.TileType;
import com.atom.models.GameSession;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class ToStringTest {
    @Test
    public void testTileToString()
    {
        Pawn myPawn = new Pawn(2, new Point(1.1,2));
        System.out.println(myPawn.toString());
        Bomb myBomb = new Bomb(2, new Point(1.1,2), 0);
        System.out.println(myBomb.toString());
    }

    @Test
    public void testPushAndGetReplica() {
        GameSession s = new GameSession(5,null);
        s.pushMessage(1, "{\"topic\":\"PLANT_BOMB\",\"data\":{}}");
        s.pushMessage(1, "{\"topic\":\"MOVE\",\"data\":{\"direction\":\"UP\"}}");
        for (int i = 0; i < 10; i++) {
            System.out.println(s.getReplica());
            try{
                sleep(1000);
            } catch (Exception e) {

            }
        }
    }

    @Test
    public void testGetReplica() {
        GameSession s = new GameSession(5,null);
        System.out.println(s.getReplica());
    }
}
