package com.riley.planetgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.riley.planetgame.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SimActivity extends AppCompatActivity {

    // Controls frame rate
    private static final long PERIOD = 17;
    // Controls how rapidly bodies increase in size per length of hold
    private static final double RAD_CONST = .03;
    // Controls how quickly bodies move relative to speed of swipe
    private static final double VEL_CONST = .02;

    private SimView sim_view;
    private List<Body> bodies;
    private List<Body> bodiesToAdd;
    private List<Body> bodiesToRemove;
    private int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim);

        sim_view = (SimView)findViewById(R.id.sim_view);
        sim_view.setOnTouchListener(new SimViewListener());

        bodies = new ArrayList<Body>();
        // These just provide places to store bodies until they can be safely added/removed
        // from the main bodies list.
        bodiesToAdd = new ArrayList<Body>();
        bodiesToRemove = new ArrayList<Body>();

        // Update the position of bodies and redraw once per period
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                // Do all of our updating of bodies here to avoid
                // concurrent modification issues
                for(Body i : bodiesToAdd){
                    bodies.add(i);
                }
                for(Body i : bodiesToRemove){
                    bodies.remove(i);
                }
                bodiesToRemove = new ArrayList<Body>();
                bodiesToAdd = new ArrayList<Body>();

                Log.v("SimActivity", "Updating positions");
                for (Body i : bodies) {
                    i.updatePos(bodies);
                    Log.v("Body position", "" + i.getPos());
                    if(i.isOutOfBounds()) {
                        Log.i("Update", "removing body");
                        bodiesToRemove.add(i);
                    }
                }
                sim_view.setBodyList(bodies);
                sim_view.invalidate();
        }}, 0, PERIOD);
    }

    private class SimViewListener implements View.OnTouchListener {
        private Vec2 initialPos;

        @Override
        public boolean onTouch(View v, MotionEvent e){
            // Update dimensions here, we know that the view has been drawn
            width = sim_view.getMeasuredWidth();
            height = sim_view.getMeasuredHeight();
            Log.i("Listener", e.getAction() + "");
            if(e.getAction() == MotionEvent.ACTION_DOWN){
                // Store where the gesture started for velocity calculation
                initialPos = new Vec2((double)e.getRawX(), (double)e.getRawY());
                return true;
            }
            if(e.getAction() == MotionEvent.ACTION_UP) {
                long downTime = e.getEventTime() - e.getDownTime();
                Log.i("Down time", "" + downTime);
                int radius = (int)(downTime * RAD_CONST);
                Vec2 finalPos = new Vec2((double)e.getRawX(), (double)e.getRawY());

                Vec2 vel = Vec2.minus(finalPos, initialPos);
                vel = vel.scale(VEL_CONST);

                bodiesToAdd.add(new Body(finalPos, vel, radius, (double)width, (double)height));
                Log.i("Adding body", "x: " + (double)e.getRawX() +
                        ", y: " + (double)e.getRawY());
                return true;
            }
            return false;
        }
    }
}