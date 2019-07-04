package com.example.inversekinematics.engine;

import com.example.inversekinematics.classes.Segment;
import com.example.inversekinematics.classes.Snake;
import com.example.inversekinematics.classes.Vector;
import com.example.inversekinematics.enums.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    public static Vector screenSize;
    private Snake snake;
    private List<Vector> food = new ArrayList<>();

    private GameState currentState = GameState.Running;
    private Random random = new Random();
    private boolean extendSnake = false;

    private Vector target = new Vector(100, 100);
    private Vector direction = new Vector();

    public GameEngine() {
    }


    public void init() {
        createSnake();

        addFood();
    }

    public void update() {
        List<Segment> body = snake.getSnake();
        if (outOfBounds()) {
            currentState = GameState.Lost;
        }

        // Get the direction of movement
        target.setX(target.getX() + direction.getX());
        target.setY(target.getY() + direction.getY());

        // Make the head follow touch
        Segment head = body.get(body.size() - 1);
        head.follow(target.getX(), target.getY());
        head.update();

        for (int i = 0; i < body.size() - 1; i++) {
            // Make the rest of the tail follow the head
            Segment current = body.get(i);
            Segment next = body.get(i + 1);
            current.follow(next.getA().getX(), next.getA().getY());
            current.update();
        }

    }

    private boolean outOfBounds() {
        return target.getX() < 0 || target.getY() < 0 || target.getX() > screenSize.getX() || target.getY() > screenSize.getY();
    }

    public Snake getSnake() {
        return snake;
    }

    private void createSnake() {
        List<Segment> body = new ArrayList<>();

        Segment head = new Segment(100, 100, 30);
        body.add(head);
        for (int i = 1; i < 10; i++) {
            body.add(new Segment(body.get(i - 1)));
        }

        snake = new Snake(body);
    }

    private void addFood() {

    }

    public Vector getTarget() {
        return target;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
