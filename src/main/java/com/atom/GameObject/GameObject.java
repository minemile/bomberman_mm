package com.atom.GameObject;

import com.atom.geometry.Point;

public abstract class GameObject {
    private int id = 0;
    private int height = 0;
    private int width = 0;
    private Point position;

    public int getId() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public GameObject(int id, int height, int width, Point position) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.position = position;
    }
}
