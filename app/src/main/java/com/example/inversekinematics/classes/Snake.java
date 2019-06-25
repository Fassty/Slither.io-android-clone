package com.example.inversekinematics.classes;

import com.example.inversekinematics.enums.SnakeState;

import java.util.List;

public class Snake {
    private List<Segment> parts;
    private Segment head;
    private SnakeState state;

    public Snake(List<Segment> parts) {
        this.parts = parts;
        this.head = parts.get(parts.size() - 1);
        state = SnakeState.ALIVE;
    }

    public SnakeState getState() {
        return state;
    }

    public void extend() {


    }

    public Segment getHead() {
        return head;
    }

    public List<Segment> getSnake() {
        return parts;
    }

    public void kill() {
        this.state = SnakeState.DEAD;
    }
}
