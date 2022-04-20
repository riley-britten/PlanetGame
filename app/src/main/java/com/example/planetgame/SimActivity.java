package com.example.planetgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SimActivity extends AppCompatActivity {

    private static final long PERIOD = 100;

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

                Log.i("SimActivity", "Updating positions");
                for (Body i : bodies) {
                    i.updatePos(bodies);
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
        @Override
        public boolean onTouch(View v, MotionEvent e){
            // Update dimensions here, we know that the view has been drawn
            width = sim_view.getMeasuredWidth();
            height = sim_view.getMeasuredHeight();
            Log.i("Listener", e.getAction() + "");
            if(e.getAction() == MotionEvent.ACTION_DOWN) {
                bodiesToAdd.add(new Body((double) e.getRawX(), (double) e.getRawY(),
                            10000, (double)width, (double)height));
                return true;
            }
            return false;
        }
    }
}