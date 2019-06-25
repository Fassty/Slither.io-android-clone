package com.example.inversekinematics.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.inversekinematics.MainActivity;
import com.example.inversekinematics.classes.Segment;
import com.example.inversekinematics.classes.Vector;

public class MainView extends View {
    Paint mPaint = new Paint();
    Segment segment;

    double targetX = 100;
    double targetY = 100;
    Vector direction = new Vector();

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setView(Segment segment) {
        this.segment = segment;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (segment != null) {

            if (targetX < 0 || targetY < 0 || targetX > getWidth() || targetY > getHeight()) {
                MainActivity.setRunning(false);
            }

            targetX += direction.getX();
            targetY += direction.getY();
            System.out.println("TARGET X : " + targetX + "|  TARGET Y : " + targetY);

            segment.follow(targetX, targetY);
            segment.update();

            mPaint.setStrokeWidth(50);
            mPaint.setAntiAlias(true);

            double cx = (segment.getA().getX() + segment.getB().getX()) / 2;
            double cy = (segment.getA().getY() + segment.getB().getY()) / 2;

            mPaint.setColor(Color.RED);
            canvas.drawCircle((float) cx, (float) cy, 35, mPaint);
            mPaint.setColor(Color.MAGENTA);
            Segment next = segment.getParent();
            while (next != null) {
                next.follow();
                next.update();

                cx = (next.getA().getX() + next.getB().getX()) / 2;
                cy = (next.getA().getY() + next.getB().getY()) / 2;

                canvas.drawCircle((float) cx, (float) cy, 35, mPaint);
                next = next.getParent();
            }
        }
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public double getTargetX() {
        return targetX;
    }

    public void setTargetX(double targetX) {
        this.targetX = targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public void setTargetY(double targetY) {
        this.targetY = targetY;
    }
}
