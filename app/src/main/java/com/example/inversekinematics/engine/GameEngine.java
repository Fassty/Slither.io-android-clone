package com.example.inversekinematics.engine;

import android.os.Build;

import com.example.inversekinematics.classes.Food;
import com.example.inversekinematics.classes.Segment;
import com.example.inversekinematics.classes.Snake;
import com.example.inversekinematics.classes.Vector;
import com.example.inversekinematics.enums.GameState;
import com.example.inversekinematics.enums.SnakeState;
import com.example.inversekinematics.enums.SnakeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    //region Static variables
    public static final int FOOD_RADIUS = 8;
    public static Vector screenSize;
    public static int timeLeft;
    private static int score;
    //endregion
    //region SpawnLocations
    private final List<Vector> spawnLocations = new ArrayList() {{
        add(new Vector(-50, -50));
        add(new Vector(screenSize.getX() / 2, -50));
        add(new Vector(screenSize.getX() + 5, -50));
        add(new Vector(-50, screenSize.getY() + 50));
        add(new Vector(screenSize.getX() / 2, screenSize.getY() + 50));
        add(new Vector(screenSize.getX() + 50, screenSize.getY() + 50));
        add(new Vector(screenSize.getX() + 50, screenSize.getY() / 2));
    }};
    //region Private variables
    private Random random = new Random();
    private GameState currentState;
    //endregion
    private int foodLimit;
    //region GameObjects
    private Snake playerSnake;
    private List<Snake> enemySnakes;
    //endregion
    private List<Food> food;
    //region Player
    private Vector target;
    private Vector direction;
    //endregion
    private int foodCount;
    //endregion

    public GameEngine() {
    }

    public static int getScore() {
        return score;
    }

    //region Public methods
    public void init() {
        enemySnakes = new ArrayList<>();
        food = new ArrayList<>();
        target = new Vector(500, 700);
        direction = new Vector(1, 0);
        score = 0;
        timeLeft = 3000;
        foodLimit = 25;
        foodCount = 0;

        createSnake();
        addEnemySnakes(5);
        addFood(25);
        currentState = GameState.Running;
    }

    public void update() {
        updateTime();

        updatePlayerPosition();

        updateEnemyPositions();

        checkForCollisions();

        removeDeadEnemies();
    }

    private void updateEnemyPositions() {
        for (Snake snake : enemySnakes) {
            Vector dir = new Vector();
            if (snake.getType() == SnakeType.Passive) {
                // Find the closest source of food that isn't being taken by another snake
                // If the food source is too close to an enemy, bail
                // If no food is safe to take, focus a random one
                dir = getSnakeDirection(snake, false);
            } else if (snake.getType() == SnakeType.Hybrid) {
                // Find the closest available food source and follow it
                dir = getSnakeDirection(snake, true);
            } else if (snake.getType() == SnakeType.Aggressive) {
                // Just follow the same target as the player
                dir = Vector.sub(Vector.add(target, direction), snake.getHead().getCenter());
                dir.setMag(20);
            }

            moveEnemy(snake, dir);

            // If enemy snake collides with player, kill it and give the player 50 points
            if (collision(snake, playerSnake)) {
                score += 50;
                snake.kill();
            }
        }
    }
    //endregion

    public void calculateFollowPoint(double touchX, double touchY) {
        Vector old = new Vector(this.target);

        Vector direction = new Vector(touchX - old.getX(), touchY - old.getY());
        direction.setMag(10);

        this.direction = direction;
    }

    //region Initialization methods
    private void createSnake() {
        List<Segment> body = new ArrayList<>();

        Segment head = new Segment(300, 600, 16);
        body.add(head);
        for (int i = 1; i < 10; i++) {
            body.add(new Segment(body.get(i - 1)));
        }

        playerSnake = new Snake(body, SnakeType.Player);
        direction = new Vector(0, 1);
        direction.setMag(10);
    }

    private void addEnemySnakes(int count) {
        for (int i = 0; i < count; i++) {
            List<Segment> body = new ArrayList<>();

            Vector pos = spawnLocations.get(random.nextInt(spawnLocations.size() - 1));

            Segment head = new Segment(pos, 16);
            body.add(head);
            for (int j = 1; j < 10; j++) {
                body.add(new Segment(body.get(j - 1)));
            }

            enemySnakes.add(new Snake(body, SnakeType.values()[random.nextInt(3) + 1]));
        }
    }

    private void addFood(int count) {
        for (int i = 0; i < count; i++) {
            food.add(new Food(getEmptyPosition()));
            foodCount++;
        }
    }

    private Vector getEmptyPosition() {
        Vector apple = new Vector(random.nextInt(1080), random.nextInt(1584));
        while (true) {
            if (screenSize == null) {
                break;
            }
            boolean free = true;
            for (Segment segment : playerSnake.getSnake()) {
                if (Vector.dist(segment.getCenter(), apple) <= playerSnake.getBodySize()) {
                    free = false;
                }
            }
            if (free) {
                return apple;
            }
            apple = new Vector(random.nextInt((int) Math.floor(screenSize.getX())), random.nextInt((int) Math.floor(screenSize.getY())));
        }

        // Safety plug before screensize is set
        return apple;
    }
    //endregion

    private void spawnFoodAt(Vector position) {
        food.add(new Food(position));
    }

    //region Update Helper-Methods
    private void updateTime() {
        timeLeft--;

        // End the game and add victory bonus
        if (timeLeft <= 0) {
            currentState = GameState.TimedOut;
            score += 5000;
        }

        // Add more food every few seconds
        if (timeLeft % 250 == 0) {
            foodLimit += 5;
        }
    }

    private void updatePlayerPosition() {
        List<Segment> body = playerSnake.getSnake();

        // Get the direction of movement
        target.setX(target.getX() + direction.getX());
        target.setY(target.getY() + direction.getY());

        // Cut the head off
        Segment head = playerSnake.getHead();

        // Make the head follow touch
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

    private void checkForCollisions() {
        checkInbounds();

        checkPlayerFoodCollision();

        // Returns the number of collided enemies so we can respawn them
        int respawnCount = checkPlayerEnemyCollisions();

        // Keep the number of enemies constant
        addEnemySnakes(respawnCount);
    }

    private boolean collision(Snake src, Snake other) {
        Segment head = src.getHead();

        for (Segment segment : other.getSnake()) {
            if (Vector.dist(head.getCenter(), segment.getCenter()) < (src.getBodySize() + other.getBodySize())) {
                return true;
            }
        }

        return false;
    }

    private void checkInbounds() {
        if (outOfBounds(target)) {
            currentState = GameState.Lost;
            playerSnake.kill();
        }
    }

    private boolean outOfBounds(Vector target) {
        if (screenSize == null) return false;
        return target.getX() < 0 || target.getY() < 0 || target.getX() > screenSize.getX() || target.getY() > screenSize.getY();
    }

    private void checkPlayerFoodCollision() {
        if (playerSnake.eat(food)) {
            foodCount--;
            score++;
        }

        // If needed, add food
        if (foodCount < foodLimit) {
            addFood(foodLimit - foodCount);
        }
    }

    private int checkPlayerEnemyCollisions() {
        int respawnCount = 0;

        // Check for enemy collisions
        for (Snake snake : enemySnakes) {
            // Remove eaten food
            if (snake.eat(food)) {
                foodCount--;
            }

            // Check for player - enemy collision
            if (collision(playerSnake, snake)) {
                currentState = GameState.Lost;
                playerSnake.kill();
            }

            // Replace dead snakes with food
            if (snake.getState() == SnakeState.DEAD) {
                for (Segment segment : snake.getSnake()) {
                    // Spawn food a little bit off center
                    for (int i = 0; i < (int) snake.getBodySize() / 10; i++) {
                        spawnFoodAt(Vector.add(segment.getCenter(), new Vector(random.nextInt(20), random.nextInt(20))));
                        foodCount++;
                    }
                }
                respawnCount++;
            }
        }

        return respawnCount;
    }


    //endregion

    private void removeDeadEnemies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            enemySnakes.removeIf(e -> e.getState() == SnakeState.DEAD);
        }
    }

    //region EnemyMovement
    private Vector getSnakeDirection(Snake snake, boolean unsafe) {
        Food closest = getClosestFood(snake, unsafe);
        Vector dir;

        // (In unsafe mode only) If player's head is closer than the closest food, attack it
        if (unsafe && Vector.dist(closest, playerSnake.getHead().getCenter()) < (Vector.dist(closest, snake.getHead().getCenter()) + playerSnake.getBodySize())) {
            dir = Vector.sub(playerSnake.getHead().getCenter(), snake.getHead().getCenter());
        } else {
            // Choose a random one if nothing is safe to eat
            if (closest == null) {
                closest = food.get(random.nextInt(food.size() - 1));
                closest.take(snake);
            } else {
                closest.take(snake);
            }
            dir = Vector.sub(closest, snake.getHead().getCenter());
        }

        dir.setMag(20);
        return dir;
    }

    private Food getClosestFood(Snake snake, boolean unsafe) {
        double minDist = Double.MAX_VALUE;
        Food closest = null;

        for (Food food : this.food) {
            double dist = Vector.dist(snake.getHead().getCenter(), food);

            // If food is not safe to eat and snake is playing safe, don't follow it
            if (!unsafe && !safeToEat(food, snake)) {
                continue;
            }
            if (dist < minDist && dist > 15 && (!food.isTaken() || food.isMine(snake))) {
                minDist = dist;
                closest = food;
            }
        }

        return closest;
    }

    private boolean safeToEat(Vector food, Snake target) {
        if (Vector.dist(this.playerSnake.getHead().getCenter(), food) < playerSnake.getBodySize() + 250) {
            return false;
        }

        return true;
    }

    //endregion

    private void moveEnemy(Snake snake, Vector dir) {
        Vector head = snake.getHead().getCenter();

        // Make the enemy follow the closest food source
        snake.getHead().follow(head.getX() + dir.getX(), head.getY() + dir.getY());
        snake.getHead().update();

        for (int i = 0; i < snake.getSnake().size() - 1; i++) {
            // Make the rest of the tail follow the head
            Segment current = snake.getSnake().get(i);
            Segment next = snake.getSnake().get(i + 1);
            current.follow(next.getA().getX(), next.getA().getY());
            current.update();
        }
    }

    //region Getters & Setters
    public Snake getPlayerSnake() {
        return playerSnake;
    }

    public List<Snake> getEnemySnakes() {
        return enemySnakes;
    }

    public List<Food> getFood() {
        return food;
    }

    public Vector getTarget() {
        return target;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public GameState getCurrentState() {
        return currentState;
    }
    //endregion
}
