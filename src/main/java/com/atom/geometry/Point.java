package com.atom.geometry;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.atom.utils.JsonHelper.toJson;

/**
 * Template class for
 */
public class Point implements Collider {
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * @param o - other object to check equality with
     * @return true if two points are equal and not null.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        // cast from Object to Point
        Point point = (Point) o;

        return ((point.x == this.x) && (point.y == this.y));
    }

    @Override
    public boolean isColliding(Collider other) {
        if (other instanceof Point) {
            Point point = (Point) other;
            return ((point.x == this.x) && (point.y == this.y));
        } else {
            Bar bar = (Bar) other;
            return ((x >= bar.getLeft().getX()) && (x <= bar.getRight().getX())
                    && (y >= bar.getLeft().getY()) && (y <= bar.getRight().getY()));
        }
    }

    public Point(double xx, double yy) {
        this.x = xx;
        this.y = yy;
    }

    @Override
    public String toString() {
        return toJson(this);
    }
}
