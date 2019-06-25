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

    public static Vector sub(Vector a, Vector b) {
        return new Vector(a.getX() - b.getX(), a.getY() - b.getY());
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.getX() + b.getX(), a.getY() + b.getY());
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

    private double getLen() {
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
}
