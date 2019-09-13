package com.example.inversekinematics.classes;

public class Vector {
    private double x;
    private double y;

    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector src) {
        this.x = src.getX();
        this.y = src.getY();
    }

    public static Vector sub(Vector a, Vector b) {
        return new Vector(a.getX() - b.getX(), a.getY() - b.getY());
    }

    public static Vector add(Vector a, Vector b) {
        if (a == null && b == null) return null;
        if (a == null) return b;
        if (b == null) return a;
        return new Vector(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public static double dist(Vector a, Vector b) {
        Vector diff = new Vector(b.getX() - a.getX(), b.getY() - a.getY());
        return diff.getLen();
    }

    public void normalize() {
        double len = getLen();
        x = getX() / len;
        y = getY() / len;
    }

    public void mult(double val) {
        this.x *= val;
        this.y *= val;
    }

    public void setMag(double len) {
        normalize();
        mult(len);
    }

    public double getLen() {
        return Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.getX(), getX()) == 0 &&
                Double.compare(vector.getY(), getY()) == 0;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
