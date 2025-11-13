package com.example.blackjack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        FindElements();
        InstantiateListener();
        SetListener();

    }

    @Override
    protected void onResume() {
        super.onResume();

        LoadSharedPreferences();
        SetPreferences();
    }

    View.OnClickListener listener;
    Button startButton, settingsButton, quitButton;
    ImageView backgroundImage;
    boolean greenBackground = true,logo;
    TextView logoText;

    private void FindElements() {
        startButton = findViewById(R.id.startButton);
        settingsButton = findViewById(R.id.settingsButton);
        quitButton = findViewById(R.id.quitButton);
        backgroundImage = findViewById(R.id.backgroundImage);
        logoText= findViewById(R.id.logoText);
    }

    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("f1", Context.MODE_PRIVATE);
        greenBackground = sharedPreferences.getBoolean("bg", true);
        logo = sharedPreferences.getBoolean("logo", true);
    }

    private void SetPreferences() {
        if (greenBackground) {
            backgroundImage.setImageResource(R.drawable.background);
        } else {
            backgroundImage.setImageResource(R.drawable.background_brown);
        }

        if (logo) {
            logoText.setVisibility(View.VISIBLE);
        } else {
            logoText.setVisibility(View.INVISIBLE);
        }
    }

    private void InstantiateListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.startButton) {
                    SwitchActivityGame();

                } else if (view.getId() == R.id.settingsButton) {
                    SwitchActivitySettings();

                } else if (view.getId() == R.id.quitButton) {
                    System.exit(0);
                }
            }
        };
    }

    private void SwitchActivitySettings() {
        Intent switchActivityIntent = new Intent(this, Settings.class);
        startActivity(switchActivityIntent);
    }

    private void SwitchActivityGame() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    private void SetListener() {
        startButton.setOnClickListener(listener);
        settingsButton.setOnClickListener(listener);
        quitButton.setOnClickListener(listener);
    }
}