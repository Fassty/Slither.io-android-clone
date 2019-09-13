package com.example.inversekinematics.classes;

public class Food extends Vector {
    private boolean taken;
    private Snake consumer;

    public Food() {
        this.setX(0);
        this.setY(0);

        this.taken = false;
    }

    public Food(double x, double y) {
        this.setX(x);
        this.setY(y);

        this.taken = false;
    }

    public Food(Vector src) {
        this.setX(src.getX());
        this.setY(src.getY());

        this.taken = false;
    }

    public boolean isTaken() {
        return taken;
    }

    public boolean isMine(Snake snake) {
        return this.consumer == snake;
    }

    public void take(Snake consumer) {
        this.taken = true;
        this.consumer = consumer;
    }

}
