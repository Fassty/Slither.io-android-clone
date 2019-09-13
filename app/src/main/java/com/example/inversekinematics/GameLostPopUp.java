package com.example.inversekinematics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.inversekinematics.engine.GameEngine;

public class GameLostPopUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gameoverpopup);

        getWindow().setLayout(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300,
                        getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500,
                        getResources().getDisplayMetrics())
        );

        FrameLayout layout = findViewById(android.R.id.content);

        TextView score = new TextView(layout.getContext());
        score.setText(String.valueOf(GameEngine.getScore()));
        score.setTextSize(80);
        score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        score.setTextColor(Color.BLACK);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 430, 0, 0);

        layout.addView(score, params);

        Button restartButton = findViewById(R.id.restartButton);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameLostPopUp.this, MainActivity.class));
            }
        });

        Button exitButton = findViewById(R.id.exitButton);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }
}
