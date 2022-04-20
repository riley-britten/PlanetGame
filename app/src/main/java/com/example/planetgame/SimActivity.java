package com.example.planetgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SimActivity extends AppCompatActivity {

    SimView sim_view;

    private Paint sim_paint;
    private List<Body> bodies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim);

        sim_view = (SimView)findViewById(R.id.sim_view);
        sim_paint = new Paint();

        Bitmap b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);

        bodies = new ArrayList<Body>();
        bodies.add(new Body(100, 100, 10, 1000, 1000));
        bodies.add(new Body(500, 500, 10, 1000, 1000));

        sim_view.setBodyList(bodies);

        sim_view.draw(c);
    }
}