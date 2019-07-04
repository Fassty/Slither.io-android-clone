package com.example.inversekinematics;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inversekinematics.classes.Vector;
import com.example.inversekinematics.engine.GameEngine;
import com.example.inversekinematics.enums.GameState;
import com.example.inversekinematics.views.MainView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private final Handler handler = new Handler();
    private final int UPDATE_DELAY = 50;
    private GameEngine gameEngine;
    private MainView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.MainView);

        gameEngine = new GameEngine();
        gameEngine.init();

        view.setOnTouchListener(this);

        startUpdateHandler();
    }

    private void startUpdateHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();

                if (gameEngine.getCurrentState() == GameState.Running) {
                    handler.postDelayed(this, UPDATE_DELAY);
                } else if (gameEngine.getCurrentState() == GameState.Lost) {
                    onGameLost();
                }

                view.setView(gameEngine.getSnake());
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
        Vector old = new Vector(gameEngine.getTarget());

        Vector direction = new Vector(touchX - old.getX(), touchY - old.getY());
        direction.setMag(10);

        gameEngine.setDirection(direction);
    }

    private void onGameLost() {
        Toast.makeText(this, "You lost!", Toast.LENGTH_SHORT).show();
    }
}
