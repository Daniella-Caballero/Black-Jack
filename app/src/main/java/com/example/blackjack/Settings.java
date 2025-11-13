package com.example.blackjack;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.Format;

public class Settings extends AppCompatActivity {

    Button ok_button, cancel_button;
    SeekBar seekBar;
    TextView minimumBetText, nameText, startingMoneyText, logoText;
    View.OnClickListener listener;
    ImageView backgroundImage;
    boolean greenBackground = false, logo = true;
    String playerName = "Player 1";
    int playerMoney = 500, minimumBet = 25;
    CheckBox logoCheckBox;
    RadioButton greenButton, brownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FindElements();
        InstantiateListener();
        SetListener();

        OnTextChangedListener();
        ControlSeekBar();
        ControlCheckBox();

        LoadSharedPreferences();
        SetPreferences();
    }

    private void ControlCheckBox() {
        logoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (logoCheckBox.isChecked()) {
                    logo = true;
                    logoText.setVisibility(View.VISIBLE);
                } else {
                    logo = false;
                    logoText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void OnTextChangedListener() {
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                playerName = String.valueOf(nameText.getText());
            }
        });

        startingMoneyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                playerMoney = Integer.parseInt(startingMoneyText.getText().toString());
            }
        });
    }

    private void FindElements() {
        logoCheckBox = findViewById(R.id.logoCheckBox);
        logoText = findViewById(R.id.logoText);
        startingMoneyText = findViewById(R.id.startingMoneyText);
        minimumBetText = findViewById(R.id.minbetval);
        seekBar = findViewById(R.id.seekBar);
        ok_button = findViewById(R.id.ok_button);
        cancel_button = findViewById(R.id.cancel_button);
        backgroundImage = findViewById(R.id.backgroundImage);
        greenButton = findViewById(R.id.greenButton);
        brownButton = findViewById(R.id.brownButton);
        nameText = findViewById(R.id.nameText);
    }

    private void SetListener() {
        cancel_button.setOnClickListener(listener);
        ok_button.setOnClickListener(listener);
        greenButton.setOnClickListener(listener);
        brownButton.setOnClickListener(listener);
    }

    private void InstantiateListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.ok_button) {
                    //safe config
                    SaveSharedPreferences();
                } else if (view.getId() == R.id.cancel_button) {
                    finish();
                } else if (view.getId() == R.id.greenButton) {
                    greenBackground = true;
                    greenButton.setChecked(true);
                    backgroundImage.setImageResource(R.drawable.background);
                } else if (view.getId() == R.id.brownButton) {
                    greenBackground = false;
                    brownButton.setChecked(true);
                    backgroundImage.setImageResource(R.drawable.background_brown);
                }
            }
        };
    }

    private void SaveSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("f1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("bg", greenBackground);
        editor.putString("name", playerName);
        editor.putInt("pm", playerMoney);
        editor.putInt("minBet", seekBar.getProgress());
        editor.putBoolean("logo", logo);

        editor.apply();
        finish();
    }

    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("f1", Context.MODE_PRIVATE);
        greenBackground = sharedPreferences.getBoolean("bg", greenBackground);
        playerName = sharedPreferences.getString("name", "Player 1");
        playerMoney = sharedPreferences.getInt("pm", 500);
        minimumBet = sharedPreferences.getInt("minBet", 25);
        logo = sharedPreferences.getBoolean("logo", true);
    }

    private void SetPreferences() {
        //bg
        if (greenBackground) {
            greenButton.setChecked(true);
            backgroundImage.setImageResource(R.drawable.background);
        } else {
            brownButton.setChecked(true);
            backgroundImage.setImageResource(R.drawable.background_brown);
        }
        //name
        nameText.setText(playerName);
        //starting money
        startingMoneyText.setText(String.valueOf(playerMoney));

        //min bet
        seekBar.setProgress(minimumBet);

        if (logo) {
            logoText.setVisibility(View.VISIBLE);
            logoCheckBox.setChecked(true);
        } else {
            logoText.setVisibility(View.INVISIBLE);
            logoCheckBox.setChecked(false);
        }
    }

    private void ControlSeekBar() {
        seekBar.setMax(250);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(5);
        }
        seekBar.setProgress(50);
        minimumBetText.setText("50");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int val, boolean b) {
                minimumBetText.setText(String.valueOf(val));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Change min bet
            }
        });
    }
}