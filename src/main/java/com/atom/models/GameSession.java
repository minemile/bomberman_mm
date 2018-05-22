package com.atom.models;

import org.json.JSONException;
import org.json.JSONObject;
import com.atom.GameObject.*;
import com.atom.geometry.Point;
import com.atom.models.DataTypes.DataDirection;
import com.atom.repositories.InputQueue;
import com.atom.tickables.Tickable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.atom.GameObject.PawnState.*;
import static com.atom.GameObject.TileType.CRATE;
import static com.atom.GameObject.TileType.WALL;
import static com.atom.utils.JsonHelper.fromJson;

public class GameSession extends Thread{
    private int id;
    private int playerCount;
    private ConcurrentHashMap<Player,Integer> players = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, List<Message>> messages = new ConcurrentHashMap<>();
    private List<GameObject> objects = new ArrayList<GameObject>();
    private int objId;
    Date lastCalled = new Date();

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    private boolean isFinished = false;

    public List<GameObject> getObjects() {
        return objects;
    }

    public GameSession(int id, List<Player> gamers) {
        this.id = id;
        this.playerCount = gamers.size();
        for (Player pl: gamers) {
            players.put(pl, 0);
        }
        initObjects(playerCount);
    }

    public boolean containsPlayer(Player player) {
        return false;
    }

    private int getObjId() {
        objId++;
        return objId;
    }

    public void pushMessage(Integer id, String m) {
        if (messages.containsKey(id)) {
            messages.get(id).add(fromJson(m, Message.class));
        } else {
            List<Message> tmp = new ArrayList<>();
            tmp.add(fromJson(m, Message.class));
            messages.put(id, tmp);
        }
    }

    /*Game field contains 27x17 tiles
            At start of the game all tiles except corner ones and
            edges are CRATEs, edges are WALLs and corners are empty
         */
    private void initObjects(int playerCount) {
        switch (playerCount) {
            case 4: objects.add(new Pawn(getObjId(), new Point(1, 15)));
            case 3: objects.add(new Pawn(getObjId(), new Point(25, 1)));
            case 2: objects.add(new Pawn(getObjId(), new Point(25, 15)));
            case 1: objects.add(new Pawn(getObjId(), new Point(1, 1)));
        }
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            objects.add(new Bonus(getObjId(), BonusType.SPEED,
                    new Point(random.nextInt(24) + 1, random.nextInt(14) + 1)));
            objects.add(new Bonus(getObjId(), BonusType.RANGE,
                    new Point(random.nextInt(24) + 1, random.nextInt(14) + 1)));
            objects.add(new Bonus(getObjId(), BonusType.BOMBS,
                    new Point(random.nextInt(24) + 1, random.nextInt(14) + 1)));
        }
        for (int i = 0; i <= 26; i++) {
            objects.add(new Tile(getObjId(), WALL, new Point(i, 0)));
            objects.add(new Tile(getObjId(), WALL, new Point(i, 16)));
        }
        for (int i = 1; i <= 15; i++) {
            objects.add(new Tile(getObjId(), WALL, new Point(0, i)));
            objects.add(new Tile(getObjId(), WALL, new Point(26, i)));
        }
        for (int j = 3; j <= 13; j++) {
            objects.add(new Tile(getObjId(), CRATE, new Point(1, j)));
            objects.add(new Tile(getObjId(), CRATE, new Point(25, j)));
        }
        for (int j = 2; j <= 14; j++) {
            objects.add(new Tile(getObjId(), CRATE, new Point(2, j)));
            objects.add(new Tile(getObjId(), CRATE, new Point(24, j)));
        }
        for (int i = 3; i <= 23; i++) {
            for (int j = 1; j <= 15; j++) {
                objects.add(new Tile(getObjId(), CRATE, new Point(i, j)));
            }
        }
    }

    public GameObject getGameObjectByPosition(Point position) {
        for (GameObject object : objects) {
            if (object.getPosition().equals(position))  {
                return object;
            }
        }
        return null;
    }

    public GameObject getGameObjectById(int id) {
        for (GameObject object : objects) {
            if (object.getId() == id)  {
                return object;
            }
        }
        return null;
    }

    public String getReplica() {
        for (ConcurrentHashMap.Entry<Integer, List<Message>> entry : messages.entrySet()) {
            for (Message mess: entry.getValue()) {
                if (mess.getTopic().equals("PLANT_BOMB")) {
                    int count = 0;
                    for (GameObject a: objects) {
                        if (a instanceof Bomb) {
                            if (((Bomb) a).getOwner() == entry.getKey()) {
                                count++;
                            }
                        }
                    }
                    if (count < ((Pawn)getGameObjectById(entry.getKey())).getBombBonus()) {
                        objects.add(new Bomb(getObjId(),
                                getGameObjectById(entry.getKey()).getPosition(),
                                entry.getKey()));
                    }
                    continue;
                }
                if (mess.getTopic().equals("MOVE")) {
                    Pawn curPawn = (Pawn)getGameObjectById(entry.getKey());
                    Point curPix = curPawn.getPix_position();
                    Point curSq = curPawn.getSq_position();

                    GameObject obstacle;
                    switch (mess.getData().toString().replace("{direction=", "")
                            .replace("}", "")) {
                        case "UP": {
                            System.out.println(5);
                            obstacle = getGameObjectByPosition(new Point(curSq.getX(), curSq.getY() - 1));
                            if ((obstacle == null) || (obstacle instanceof Pawn) || (curPix.getY() % 32 > 16)) {
                                curPawn.setState(UP);
                            }
                            break;
                        }
                        case "DOWN": {
                            obstacle = getGameObjectByPosition(new Point(curSq.getX(), curSq.getY() + 1));
                            if ((obstacle == null) || (obstacle instanceof Pawn) || (curPix.getY() % 32 < 16)) {
                                curPawn.setState(DOWN);
                            }
                            break;
                        }
                        case "LEFT": {
                            obstacle = getGameObjectByPosition(new Point(curSq.getX() - 1, curSq.getY()));
                            if ((obstacle == null) || (obstacle instanceof Pawn) || (curPix.getX() % 32 > 16)) {
                                curPawn.setState(LEFT);
                            }
                            break;
                        }
                        case "RIGHT": {
                            obstacle = getGameObjectByPosition(new Point(curSq.getX() + 1, curSq.getY()));
                            if ((obstacle == null) || (obstacle instanceof Pawn) || (curPix.getX() % 32 < 16)) {
                                curPawn.setState(RIGHT);
                            }
                            break;
                        }
                    }
                }
            }
        }
        messages.clear();
        Date now = new Date();
        long elapsed = now.getTime() - lastCalled.getTime();

        List<GameObject> newObjects = new ArrayList<GameObject>();
        for (Iterator<GameObject> iterator = objects.iterator(); iterator.hasNext();) {
            GameObject a = iterator.next();
            if (a instanceof Bomb) {
                Bomb curBomb = (Bomb)a;
                Point curPos = curBomb.getPosition();
                if (!(curBomb.isAlive())) {
                    int force = ((Pawn)getGameObjectById(curBomb.getOwner())).getForceBonus() + 1;
                    GameObject obstacle;
                    for (int i = 0; i < force; i++) {
                        obstacle = getGameObjectByPosition(new Point(curPos.getX() + i, curPos.getY()));
                        if ((obstacle == null) || (obstacle instanceof Pawn)) {
                            newObjects.add(new Fire(getObjId(), new Point(curPos.getX() + i, curPos.getY())));
                        }
                    }
                    for (int i = 0; i < force; i++) {
                        obstacle = getGameObjectByPosition(new Point(curPos.getX() - i, curPos.getY()));
                        if ((obstacle == null) || (obstacle instanceof Pawn)) {
                            newObjects.add(new Fire(getObjId(), new Point(curPos.getX() - i, curPos.getY())));
                        }
                    }
                    for (int i = 0; i < force; i++) {
                        obstacle = getGameObjectByPosition(new Point(curPos.getX(), curPos.getY() + i));
                        if ((obstacle == null) || (obstacle instanceof Pawn)) {
                            newObjects.add(new Fire(getObjId(), new Point(curPos.getX(), curPos.getY() + i)));
                        }
                    }
                    for (int i = 0; i < force; i++) {
                        obstacle = getGameObjectByPosition(new Point(curPos.getX(), curPos.getY() - i));
                        if ((obstacle == null) || (obstacle instanceof Pawn)) {
                            newObjects.add(new Fire(getObjId(), new Point(curPos.getX(), curPos.getY() - i)));
                        }
                    }
                    iterator.remove();
                } else {
                    ((Bomb)a).tick(elapsed);
                }
                continue;
            }
            if (a instanceof Pawn) {
                ((Pawn)a).tick(elapsed);
                ((Pawn)a).setState(IDLE);
                continue;
            }
            if (a instanceof Fire) {
                for (GameObject b: objects) {
                    if ((b instanceof Pawn) && (b.getPosition().isColliding(a.getPosition()))) {
                        ((Pawn) b).setState(DEAD);
                    }
                    if ((b instanceof Tile) && (b.getPosition().isColliding(a.getPosition()))
                            && (((Tile) b).getType() == CRATE)) {
                        iterator.remove();
                    }
                }
                if (!(((Fire)a).isAlive())) {
                    iterator.remove();
                } else {
                    ((Fire)a).tick(elapsed);
                }
            }
        }
        objects.addAll(newObjects);
        lastCalled = now;

        JSONObject resultJson = new JSONObject();

        try {
            resultJson.put("objects", objects.toString());
            resultJson.put("gameOver", isFinished);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject replica = new JSONObject();

        try {
            replica.put("data", resultJson);
            replica.put("topic", "REPLICA");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return replica.toString().replace("\\\"", "\"");
    }

    @Override
    public void run() {
        int FRAMES = 30;
        while (!isFinished) {
            try {
                sleep(1000/FRAMES);
            } catch(Exception e) {
                System.out.println("GameSession interrupted" + e.toString());
                return;
            }
            //broadcast(wsSession, getReplica());
        }
    }
}
