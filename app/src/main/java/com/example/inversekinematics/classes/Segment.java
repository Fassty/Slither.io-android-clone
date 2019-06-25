package com.example.inversekinematics.classes;

public class Segment {
    Segment parent = null;
    Segment child;
    private Vector a;
    private Vector b = new Vector();
    private double angle;
    private double len;

    public Segment(double x, double y, double len) {
        this.a = new Vector(x, y);
        this.len = len;

        calculateEnd();
    }

    public Segment(Segment parent, double len) {
        this.parent = parent;
        this.a = new Vector(parent.b.getX(), parent.b.getY());
        this.len = len;
        calculateEnd();
    }

    // Polar to cartesian
    private void calculateEnd() {
        double dx = len * Math.cos(angle);
        double dy = len * Math.sin(angle);
        b.setX(a.getX() + dx);
        b.setY(a.getY() + dy);
    }

    public void follow() {
        double targetX = child.a.getX();
        double targetY = child.a.getY();

        follow(targetX, targetY);
    }

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

    public void update() {
        calculateEnd();
    }

    public Segment getChild() {
        return child;
    }

    public void setChild(Segment child) {
        this.child = child;
    }

    public Segment getParent() {
        return parent;
    }

    public void setParent(Segment parent) {
        this.parent = parent;
    }

    public Vector getA() {
        return a;
    }

    public Vector getB() {
        return b;
    }
}
