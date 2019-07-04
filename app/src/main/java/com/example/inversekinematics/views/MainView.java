package com.example.inversekinematics.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.inversekinematics.classes.Segment;
import com.example.inversekinematics.classes.Snake;
import com.example.inversekinematics.classes.Vector;
import com.example.inversekinematics.engine.GameEngine;

import java.util.List;

public class MainView extends View {
    private final int BODY_RADIUS = 35;
    private final int FOOD_RADIUS = 35;
    boolean sizeSet = false;
    private Paint mPaint = new Paint();
    private Snake snake;
    private List<Vector> apples;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setView(Snake snake) {
        this.snake = snake;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!sizeSet) {
            GameEngine.screenSize = new Vector(getWidth(), getHeight());
        }

        if (snake != null && snake.getSnake() != null) {
            for (Segment segment : snake.getSnake()) {
                mPaint.setColor(Color.DKGRAY);
                canvas.drawCircle((float) segment.getCenter().getX(), (float) segment.getCenter().getY(), BODY_RADIUS, mPaint);
            }
        }
    }
}
