package com.riley.planetgame;

import android.util.Log;

import java.util.List;

public class Body {
    private double minX, maxX, minY, maxY, mass, radius;
    // Gravitational constant
    private static final double G = (double) 0.1;
    // Min and max body radius
    private static final double MIN_R = 2;
    private static final double MAX_R = 50;

    private Vec2 pos, vel;

    public Body(Vec2 pos, Vec2 vel, double radius, double maxX, double maxY){
        this.pos = pos;
        this.vel = vel;
        this.radius = Body.clampRadius(radius);
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = 0;
        this.minY = 0;
        this.mass = ((float)4/(float)3) * Math.PI * Math.pow(radius, 3);
    }

    public double getPosX(){
        return pos.x;
    }

    public double getPosY(){
        return pos.y;
    }

    public Vec2 getPos(){
        return pos;
    }

    public double getMass(){
        return mass;
    }

    public Vec2 getVel(){
        return vel;
    }

    public double getRadius(){
        return radius;
    }

    public String toString(){
        return pos.x + ", " + pos.y;
    }

    public boolean isOutOfBounds(){
        if(pos.x > maxX || pos.x < 0){
            return true;
        }else if(pos.y > maxY || pos.y < 0){
            return true;
        }
        return false;
    }

    public void updatePos(List<Body> bodies){
        Log.v("Body", "position being updated");
        for(Body i : bodies){
            if(i == this){
                continue;
            }
            if(Vec2.dist(pos, i.pos) < radius + i.getRadius()){
                //Bodies collide
                vel.x = collision(i).x;
                vel.y = collision(i).y;
                break;
            }else{
                // Bodies don't collide, only change in velocity is from gravity
                vel = Vec2.plus(vel, accelerationFromGravity(i));
            }
        }
        pos = Vec2.plus(pos, vel);
    }

    private Vec2 collision(Body other){
        double m1 = mass;
        double m2 = other.getMass();
        Vec2 x1 = pos;
        Vec2 v1 = vel;
        Vec2 x2 = other.getPos();
        Vec2 v2 = other.getVel();

        // From angle free formula on elastic collision wikipedia
        double coeff = (-2 * m2)/(m1 + m2) * (Vec2.dot(Vec2.minus(v1, v2),
                Vec2.minus(x1, x2))/(Vec2.dot(Vec2.minus(x1, x2),
                Vec2.minus(x1, x2))));

        Vec2 retVal = Vec2.minus(x1, x2);
        retVal = retVal.scale(coeff);
        retVal = Vec2.minus(v1, retVal);
        return retVal;
    }

    private Vec2 accelerationFromGravity(Body other){
        Log.i("Computing acceleration", "Positions: " + pos + " " + other.getPos());
        double d = Vec2.dist(pos, other.pos);
        double a = G * (other.mass/Math.pow(d, 2));
        Log.i("Computing acceleration", ""+Vec2.minus(other.pos, pos).scale(a/d));
        Log.i("Acceleration params", "d: " + d + ", a: " + a + ", v" + Vec2.minus(other.pos, pos).normalize());
        return Vec2.minus(other.pos, pos).normalize().scale(a);
    }

    private static double clampRadius(double r){
        if(r < MIN_R){
            return MIN_R;
        }else if(r > MAX_R){
            return MAX_R;
        }
        return r;
    }
}