package com.riley.planetgame;

import android.util.Log;

public class Vec2 {
    public double x, y;

    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double norm(){
        return Math.sqrt(Vec2.dot(this, this));
    }

    public Vec2 scale(double s){
        return new Vec2(x * s, y * s);
    }

    public String toString(){
        return "(" + x + ", " + y +")";
    }

    public Vec2 normalize(){
        Log.v("Normalizing " + this, "" + this.norm() + " " + this.scale(1/this.norm()));
        return this.scale(1/(this.norm()));
    }

    public static double dot(Vec2 a, Vec2 b){
        return a.x * b.x + a.y * b.y;
    }
    public static Vec2 plus(Vec2 a, Vec2 b){
        return new Vec2(a.x + b.x, a.y + b.y);
    }

    public static Vec2 minus(Vec2 a, Vec2 b){
        return new Vec2(a.x - b.x, a.y - b.y);
    }

    public static double dist(Vec2 a, Vec2 b){
        return Vec2.minus(a, b).norm();
    }
}
