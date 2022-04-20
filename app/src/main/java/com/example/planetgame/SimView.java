package com.example.planetgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class SimView extends View {
    private Paint green = new Paint();
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

    public void setBodyList(List<Body> bodies){
        this.bodies = bodies;
    }

    @Override
    public void onDraw(Canvas c){
        for(Body i : bodies){
            c.drawCircle((float)i.getPosX(), (float)i.getPosY(), (float)i.getRadius(), green);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}