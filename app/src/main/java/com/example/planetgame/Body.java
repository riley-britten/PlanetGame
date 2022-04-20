package com.example.planetgame;

import android.util.Log;

import java.util.List;

public class Body {
    private double posX, posY, vX, vY, minX, maxX, minY, maxY, mass, radius;
    private static final double G = 1;

    public Body(double posX, double posY, double mass, double maxX, double maxY){
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
        this.maxX = maxX;
        this.maxY = maxY;
        this.vX = 0;
        this.vY = 0;
        this.minX = 0;
        this.minY = 0;
        this.radius = 10;
    }

    public double getPosX(){
        return posX;
    }

    public double getPosY(){
        return posY;
    }

    public double getRadius(){
        return radius;
    }

    public String toString(){
        return posX + ", " + posY;
    }

    public boolean isOutOfBounds(){
        if(posX > maxX || posX < 0){
            return true;
        }else if(posY > maxY || posY < 0){
            return true;
        }
        return false;
    }

    public void updatePos(List<Body> bodies){
        Log.i("Body", "position being updated");
        double oldX = this.posX;
        double oldY = this.posY;
        for(Body i : bodies){
            if(i == this){
                continue;
            }
            this.vX += xAccelerationFromOther(i);
            this.vY += yAccelerationFromOther(i);
        }
        this.posX += this.vX;
        this.posY += this.vY;
        if(this.posX == oldX && this.posY == oldY){
            Log.e("Body", "position has not changed");
        }
    }

    private double distance(Body other){
        return Math.sqrt(Math.pow(other.getPosX() - this.posX, 2) + Math.pow(other.getPosY() - this.posY, 2));
    }

    private double xAccelerationFromOther(Body other){
        double d = distance(other);
        double a = G * (other.mass/Math.pow(d, 2));
        Log.i("Computing acceleration", ""+a);
        return a * (other.getPosX() - this.posX)/d;
    }

    private double yAccelerationFromOther(Body other){
        double d = distance(other);
        double a = G * (other.mass/Math.pow(d, 2));
        return a * (other.getPosY() - this.posY)/d;
    }
}