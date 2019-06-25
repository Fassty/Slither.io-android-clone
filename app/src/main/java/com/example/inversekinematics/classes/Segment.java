package com.example.inversekinematics.classes;

public class Segment {
    private Vector a;
    private Vector b = new Vector();
    private double angle;
    private double len;

    public Segment(double x, double y, double len) {
        this.a = new Vector(x, y);
        this.len = len;

        calculateEnd();
    }

    public Segment(Segment parent) {
        this.a = new Vector(parent.b);
        this.len = parent.len;
        calculateEnd();
    }

    /**
     * Calculates the end-point of the segment in polar coordinates based on the length
     */
    private void calculateEnd() {
        // Polar to cartesian
        double dx = len * Math.cos(angle);
        double dy = len * Math.sin(angle);

        b.setX(a.getX() + dx);
        b.setY(a.getY() + dy);
    }

    /**
     * Makes the segment follow certain coordinate
     */
    public void follow(double targetX, double targetY) {
        double dy = targetY - a.getY();
        double dx = targetX - a.getX();
        angle = Math.atan2(dy, dx);

        Vector target = new Vector(targetX, targetY);
        Vector dir = Vector.sub(target, a);

        dir.setMag(len);
        dir.mult(-1);

        a = Vector.add(target, dir);
    }

    /**
     * Recalculates the end-point of given segment
     */
    public void update() {
        calculateEnd();
    }

    /**
     * Getter for the front-point of the segment
     *
     * @return
     */
    public Vector getA() {
        return a;
    }

    /**
     * Getter for the end-point of the segment
     * @return
     */
    public Vector getB() {
        return b;
    }
}
