package com.example.inversekinematics.classes;

import com.example.inversekinematics.enums.SnakeState;
import com.example.inversekinematics.enums.SnakeType;

import java.util.List;

public class Snake {
    private List<Segment> parts;
    private SnakeState state;
    private SnakeType type;
    private double angle;
    private float bodySize = 16;
    private int score = 1;

    public Snake(List<Segment> parts, SnakeType type) {
        this.parts = parts;
        this.type = type;
        state = SnakeState.ALIVE;
    }

    public SnakeState getState() {
        return state;
    }

    public SnakeType getType() {
        return type;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double newAngle) {
        angle = newAngle;
    }

    public void rotateHead(double angleOfRotation) {
        angle += angleOfRotation;
        while (Math.abs(angle) >= Math.PI) {
            angle -= Math.PI * Math.signum(angle);
        }
    }

    public Segment getHead() {
        return parts.get(parts.size() - 1);
    }

    public float getBodySize() {
        return bodySize;
    }

    public boolean eat(List<Food> apples) {
        Vector toRemove = null;

        for (Vector apple : apples) {
            if (Vector.dist(apple, parts.get(parts.size() - 1).getCenter()) <= bodySize) {
                toRemove = apple;
            }
        }

        if (toRemove != null) {
            apples.remove(toRemove);
            extend();
            return true;
        }

        return false;
    }

    private void extend() {
        score++;
        if (score % 4 == 0) {
            parts.add(0, new Segment(parts.get(0)));
            if (bodySize < 80) bodySize *= 1.02;
        }
    }

    public List<Segment> getSnake() {
        return parts;
    }

    public void kill() {
        this.state = SnakeState.DEAD;
    }
}
