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

    private boolean to_delete = false;
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

    public boolean shouldDelete(){
        return to_delete;
    }

    public void addVel(Vec2 v){
        vel = Vec2.plus(vel, v);
    }

    public void addMass(double m){
        mass += m;
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
            if(collideBeforeNextFrame(i) && mass <= i.getMass()
                    && !i.shouldDelete()){
                // Delete smaller body
                to_delete = true;
                // Add momentum of smaller body to larger
                vel = vel.scale(mass/(mass + i.getMass()));
                i.addVel(vel);
                i.addMass(mass);
                break;
            }else{
                // Bodies don't collide, only change in velocity is from gravity
                vel = Vec2.plus(vel, accelerationFromGravity(i));
            }
        }
        pos = Vec2.plus(pos, vel);
        // Since mass can change have to update radius to match
        radius = Math.pow(0.75 * mass/Math.PI, 1.0/3.0);
    }

    private boolean collideBeforeNextFrame(Body other){
        Vec2 q = other.getPos();
        double a = Vec2.dot(vel, vel);
        double b = 2 * vel.x * (pos.x - q.x) + 2 + vel.y * (pos.y - q.y);
        double c = Math.pow(pos.x - q.x, 2) + Math.pow(pos.y - q.y, 2)
                - Math.pow(radius + other.getRadius(), 2);
        double t1 = (Math.sqrt(Math.pow(b, 2) - 4 * a * c) - b)/(2 * a);
        double t2 = (Math.sqrt(Math.pow(b, 2) - 4 * a * c) + b)/(2 * a);
        if(0 <= t1 && t1 < 1){
            return true;
        }else if(0 <= t2 && t2 < 1){
            return true;
        }
        return false;
    }

/*    private Vec2 collision(Body other){
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
    }*/

    private Vec2 accelerationFromGravity(Body other){
        Log.i("Computing acceleration", "Positions: " + pos + " " + other.getPos());
        double d = Vec2.dist(pos, other.pos);
        if(d < radius + other.getRadius()){
            // Don't compute acceleration from gravity if bodies
            // have already collided.
            return new Vec2(0, 0);
        }
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