package com.example.planetgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class SimView extends View {
    private Paint sim_paint = new Paint();
    private List<Body> bodies;

    public SimView(Context context) {
        super(context);
    }

    public SimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // get view dimensions suggested by layout
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // get screen size
        int displayWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;

        // clamp dimensions to at most screen dimensions
        width = Math.min(width, displayWidth);
        height = Math.min(height, displayHeight);
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );
    }

    public void setBodyList(List<Body> bodyList){
        this.bodies = bodyList;
    }

    @Override
    public void onDraw(Canvas c){
        Log.v("SimView", "redrawing canvas");
        c.drawColor(Color.BLACK);
        sim_paint.setColor(Color.WHITE);
        for(Body i : bodies){
            c.drawCircle((float)i.getPosX(), (float)i.getPosY(), (float)i.getRadius(), sim_paint);
        }
    }
}