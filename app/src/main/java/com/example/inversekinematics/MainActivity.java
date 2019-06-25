package com.example.inversekinematics;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inversekinematics.classes.Segment;
import com.example.inversekinematics.classes.Vector;
import com.example.inversekinematics.views.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    static boolean running = true;
    private final Handler handler = new Handler();
    private final int UPDATE_DELAY = 50;
    private List<Segment> tentacle = new ArrayList<>();
    MainView view;

    public static void setRunning(boolean runn) {
        running = runn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Segment head = new Segment(100, 100, 30);
        tentacle.add(head);
        for (int i = 1; i < 10; i++) {
            tentacle.add(new Segment(tentacle.get(i - 1)));
        }

        view = findViewById(R.id.MainView);
        view.setOnTouchListener(this);

        startUpdateHandler();
    }

    private void startUpdateHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    handler.postDelayed(this, UPDATE_DELAY);
                }

                if (!running) {
                    onGameLost();
                }

                view.setView(tentacle);
                view.invalidate();
            }
        }, UPDATE_DELAY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                calculateFollowPoint(e.getX(), e.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                calculateFollowPoint(e.getX(), e.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private void calculateFollowPoint(double touchX, double touchY) {
        Vector old = new Vector(view.getTarget());

        Vector direction = new Vector(touchX - old.getX(), touchY - old.getY());
        direction.setMag(10);

        view.setDirection(direction);
    }

    private void onGameLost() {
        Toast.makeText(this, "You lost!", Toast.LENGTH_SHORT).show();
    }
}
