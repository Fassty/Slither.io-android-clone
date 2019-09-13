package com.example.inversekinematics;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inversekinematics.engine.GameEngine;
import com.example.inversekinematics.enums.GameState;
import com.example.inversekinematics.views.MainView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private final Handler handler = new Handler();
    private final int UPDATE_DELAY = 50;
    private final double QUATERNION = Math.PI / 4;
    private GameEngine gameEngine;
    private MainView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getSupportActionBar().hide();
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
                } else if (gameEngine.getCurrentState() == GameState.TimedOut) {
                    onGameEnded();
                }

                view.setView(gameEngine.getPlayerSnake(), gameEngine.getEnemySnakes(), gameEngine.getFood());
                view.invalidate();
            }
        }, UPDATE_DELAY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gameEngine.calculateFollowPoint(e.getX(), e.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                gameEngine.calculateFollowPoint(e.getX(), e.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private void onGameLost() {

        startActivity(new Intent(MainActivity.this, GameLostPopUp.class));

    }

    private void onGameEnded() {
        Toast.makeText(this, "You win!", Toast.LENGTH_LONG).show();
    }
}
