package com.example.planetgame;

import java.util.List;

public class Body {
    private double posX, posY, vX, vY, minX, maxX, minY, maxY, mass, radius;
    private static final double G = .1;

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
        this.radius = 100;
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

    public void updatePos(List<Body> bodies){
        for(Body i : bodies){
            if(i == this){
                continue;
            }
            this.posX += xAccelerationFromOther(i);
            this.posY += yAccelerationFromOther(i);
        }
    }

    private double distance(Body other){
        return Math.sqrt(Math.pow(other.getPosX() - this.posX, 2) + Math.pow(other.getPosY() - this.posY, 2));
    }

    private double xAccelerationFromOther(Body other){
        double d = distance(other);
        double a = G * (other.mass/Math.pow(d, 2));
        return a * (other.getPosX() - this.posX)/d;
    }

    private double yAccelerationFromOther(Body other){
        double d = distance(other);
        double a = G * (other.mass/Math.pow(d, 2));
        return a * (other.getPosY() - this.posY)/d;
    }
}