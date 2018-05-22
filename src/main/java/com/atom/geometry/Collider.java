package com.atom.geometry;

/**
 * Entity that can physically intersect, like flame and player
 */
public interface Collider {
    boolean isColliding(Collider other);
}