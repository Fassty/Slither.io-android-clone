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

import java.util.List;

public class MainView extends View {
    Paint mPaint = new Paint();
    List<Segment> segment;

    Vector target = new Vector(100, 100);
    Vector direction = new Vector();

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setView(List<Segment> segment) {
        this.segment = segment;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (segment != null) {

            // Check for
            if (target.getX() < 0 || target.getY() < 0 || target.getX() > getWidth() || target.getY() > getHeight()) {
                MainActivity.setRunning(false);
            }

            // Get the direction of movement
            target.setX(target.getX() + direction.getX());
            target.setY(target.getY() + direction.getY());
            System.out.println("TARGET X : " + target.getX() + "|  TARGET Y : " + target.getY());

            // Make the head follow touch
            Segment head = segment.get(segment.size() - 1);
            head.follow(target.getX(), target.getY());
            head.update();

            // Get the center of the head segment for display
            double cx = (head.getA().getX() + head.getB().getX()) / 2;
            double cy = (head.getA().getY() + head.getB().getY()) / 2;
            mPaint.setColor(Color.RED);
            canvas.drawCircle((float) cx, (float) cy, 35, mPaint);

            for (int i = 0; i < segment.size() - 1; i++) {
                // Make the rest of the tail follow the head
                Segment current = segment.get(i);
                Segment next = segment.get(i + 1);
                current.follow(next.getA().getX(), next.getA().getY());
                current.update();

                // Calculate circle centers
                cx = (current.getA().getX() + current.getB().getX()) / 2;
                cy = (current.getA().getY() + current.getB().getY()) / 2;
                mPaint.setColor(Color.DKGRAY);
                canvas.drawCircle((float) cx, (float) cy, 35, mPaint);
            }
        }
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Vector getTarget() {
        return target;
    }

    public void setTarget(Vector target) {
        this.target = target;
    }
}
