package com.example.blackjack;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView moneyText, betTextEdit, playerScoreText, dealerScoreText, makeYourBetText, nameText, logoText;
    Button drawButton, standButton;
    LinearLayout dealerCardsLayout, playerCardsLayout;
    ArrayList<ImageView> dealerCards, playerCards;
    View.OnClickListener listener;

    int money = 500, minimumBet, playerScore, dealerScore;
    boolean hasBetted = false,playerHasStood = false, dealerGotAce = false, playerGotAce = false, greenBackground = false, logo = true;
    String playerName;

    ImageView backgroundImage, fakeCard;

    int[] suit = {
            R.drawable.club_resized,
            R.drawable.diamond_resized,
            R.drawable.heart_resized,
            R.drawable.spade_resized
    };

    int[] number = {
            R.drawable.a,
            R.drawable.c2,
            R.drawable.c3,
            R.drawable.c4,
            R.drawable.c5,
            R.drawable.c6,
            R.drawable.c7,
            R.drawable.c8,
            R.drawable.c9,
            R.drawable.c10,
            R.drawable.j,
            R.drawable.q,
            R.drawable.k,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindElements();
        InstantiateListener();
        SetListener();

        LoadSharedPreferences();
        SetPreferences();
    }

    private void InstantiateListener() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.drawButton) {
                    if (hasBetted&&!playerHasStood) {
                        if (playerScore <= 21) {
                            PlayerDrawCard();
                        }
                    } else {
                        makeBet();
                    }
                } else if (view.getId() == R.id.standButton) {
                    //dealer end play

                    if (!playerHasStood && playerScore != 0) {
                        playerHasStood = true;
                        dealerCardsLayout.removeViewAt(1);
                        if (!dealerGotAce) {
                            while (dealerScore <= 16) {
                                DealerDrawCard();
                            }
                        }else {
                            while (dealerScore <= 16) {
                                DealerDrawCard();
                            }
                        }
                    }
                    //decidir tu score
                    //comparar score--> decidir winner-->gain/lose money-->reset game-->new game
                }
            }
        };
    }

    private void makeBet() {
        if (Integer.parseInt(String.valueOf(betTextEdit.getText())) < minimumBet || Integer.parseInt(String.valueOf(betTextEdit.getText())) > money) {
            Toast.makeText(getApplicationContext(), "Can't be lower than " + minimumBet + '\n' + "Can't be more than your money!", Toast.LENGTH_SHORT).show();
            betTextEdit.setText(String.valueOf(minimumBet));
        } else {
            makeYourBetText.setVisibility(View.INVISIBLE);
            betTextEdit.setEnabled(false);
            hasBetted = true;
            StartGame();
        }
    }

    void StartGame() {
        PlayerDrawCard();
        PlayerDrawCard();
        DealerDrawCard();
        DealerDrawFaceDownCard();
    }

    void resetGame() {

        SetPreferences();
        playerHasStood = false;
        hasBetted = false;
        playerGotAce = false;
        dealerGotAce = false;
        betTextEdit.setEnabled(true);
        makeYourBetText.setVisibility(View.VISIBLE);
        fakeCard = null;

        //player
        //reset score
        playerScore = 0;
        playerScoreText.setText("0");
        //reset cards
        playerCards.clear();
        playerCardsLayout.removeAllViews();

        //dealer
        //reset score
        dealerScore = 0;
        dealerScoreText.setText("0");
        //reset cards
        dealerCards.clear();
        dealerCardsLayout.removeAllViews();
    }

    private void PlayerDrawCard() {
        ImageView card = new ImageView(this);

        int indexSuit = (int) (Math.random() * suit.length);
        int indexNumber = (int) (Math.random() * number.length);

        switch (indexNumber) {
            case 0:
                addPlayerScore(1);
                break;
            case 1:
                addPlayerScore(2);
                break;
            case 2:
                addPlayerScore(3);
                break;
            case 3:
                addPlayerScore(4);
                break;
            case 4:
                addPlayerScore(5);
                break;
            case 5:
                addPlayerScore(6);
                break;
            case 6:
                addPlayerScore(7);
                break;
            case 7:
                addPlayerScore(8);
                break;
            case 8:
                addPlayerScore(9);
                break;
            case 9:
            case 12:
            case 11:
            case 10:
                addPlayerScore(10);
                break;
            default:
                break;
        }

        Bitmap s = BitmapFactory.decodeResource(getResources(), suit[indexSuit]);
        Bitmap n = BitmapFactory.decodeResource(getResources(), number[indexNumber]);

        card.setImageBitmap(combineImages(s, n));
        card.setBackground(getDrawable(R.drawable.front));

        card.setLayoutParams(new ViewGroup.LayoutParams(200, 255));
        playerCardsLayout.addView(card);
        playerCards.add(card);
    }

    private void addPlayerScore(int number) {
        if (number == 1) {
            //Ace
            playerGotAce = true;
            playerScore += number;
            playerScoreText.setText(String.valueOf(playerScore) + " / " + String.valueOf(playerScore + 10));
        } else {
            playerScore += number;
            if (!playerGotAce) {
                playerScoreText.setText(String.valueOf(playerScore));
            } else {
                playerScoreText.setText(String.valueOf(playerScore) + " / " + String.valueOf(playerScore + 10));
            }
        }
    }


    private void DealerDrawFaceDownCard() {
        ImageView card = new ImageView(this);

        int indexSuit = (int) (Math.random() * suit.length);
        int indexNumber = (int) (Math.random() * number.length);

        switch (indexNumber) {
            case 0:
                addDealerScore(1);
                break;
            case 1:
                addDealerScore(2);
                break;
            case 2:
                addDealerScore(3);
                break;
            case 3:
                addDealerScore(4);
                break;
            case 4:
                addDealerScore(5);
                break;
            case 5:
                addDealerScore(6);
                break;
            case 6:
                addDealerScore(7);
                break;
            case 7:
                addDealerScore(8);
                break;
            case 8:
                addDealerScore(9);
                break;
            case 9:
            case 10:
            case 12:
            case 11:
                addDealerScore(10);
                break;
            default:
                break;
        }

        //
        Bitmap s = BitmapFactory.decodeResource(getResources(), suit[indexSuit]);
        Bitmap n = BitmapFactory.decodeResource(getResources(), number[indexNumber]);


        //card.setImageBitmap(combineImages(s, n));
        //
        card.setBackground(getDrawable(R.drawable.back));

        card.setLayoutParams(new ViewGroup.LayoutParams(200, 255));
        dealerCardsLayout.addView(card);
        //dealerCards.add(card);
        fakeCard = card;
    }

    private void DealerDrawCard() {
        ImageView card = new ImageView(this);

        int indexSuit = (int) (Math.random() * suit.length);
        int indexNumber = (int) (Math.random() * number.length);

        switch (indexNumber) {
            case 0:
                addDealerScore(1);
                dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                break;
            case 1:
                addDealerScore(2);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 2:
                addDealerScore(3);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 3:
                addDealerScore(4);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 4:
                addDealerScore(5);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 5:
                addDealerScore(6);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 6:
                addDealerScore(7);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 7:
                addDealerScore(8);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 8:
                addDealerScore(9);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            case 9:
            case 10:
            case 12:
            case 11:
                addDealerScore(10);
                if (!dealerGotAce) {
                    dealerScoreText.setText(String.valueOf(dealerScore));
                } else {
                    dealerScoreText.setText(String.valueOf(dealerScore) + " / " + String.valueOf(dealerScore + 10));
                }
                break;
            default:
                break;
        }

        Bitmap s = BitmapFactory.decodeResource(getResources(), suit[indexSuit]);
        Bitmap n = BitmapFactory.decodeResource(getResources(), number[indexNumber]);

        card.setImageBitmap(combineImages(s, n));
        card.setBackground(getDrawable(R.drawable.front));

        card.setLayoutParams(new ViewGroup.LayoutParams(200, 255));
        dealerCardsLayout.addView(card);
        dealerCards.add(card);
    }

    private void addDealerScore(int number) {
        if (number == 1) {
            dealerGotAce = true;
        }
        dealerScore += number;
    }

    public Bitmap combineImages(Bitmap topImage, Bitmap bottomImage) {
        Bitmap overlay = Bitmap.createBitmap(550, 700, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bottomImage, 210, 415, null);
        canvas.drawBitmap(topImage, 75, 55, null);
        return overlay;
    }

    private void LoadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("f1", Context.MODE_PRIVATE);
        greenBackground = sharedPreferences.getBoolean("bg", greenBackground);
        playerName = sharedPreferences.getString("name", "Player 1");
        money = sharedPreferences.getInt("pm", 500);
        minimumBet = sharedPreferences.getInt("minBet", 25);
        logo = sharedPreferences.getBoolean("logo", true);
    }

    private void SetPreferences() {
        //bg
        if (greenBackground) {
            backgroundImage.setImageResource(R.drawable.background);
        } else {
            backgroundImage.setImageResource(R.drawable.background_brown);
        }
        //name
        nameText.setText(playerName);
        //starting money
        moneyText.setText(String.valueOf(money));
        //min bet
        betTextEdit.setText(String.valueOf(minimumBet));

        if (logo) {
            logoText.setVisibility(View.VISIBLE);
        } else {
            logoText.setVisibility(View.INVISIBLE);
        }
    }

    private void FindElements() {
        dealerCards = new ArrayList<ImageView>();
        playerCards = new ArrayList<ImageView>();

        backgroundImage = findViewById(R.id.backgroundImage);
        logoText = findViewById(R.id.logoText);
        nameText = findViewById(R.id.nameText);

        makeYourBetText = findViewById(R.id.makeYourBetText);
        moneyText = findViewById(R.id.moneyText);
        betTextEdit = findViewById(R.id.betTextEdit);
        playerScoreText = findViewById(R.id.playerScoreText);
        dealerScoreText = findViewById(R.id.dealerScoreText);
        drawButton = findViewById(R.id.drawButton);
        standButton = findViewById(R.id.standButton);
        dealerCardsLayout = findViewById(R.id.dealerCards);
        playerCardsLayout = findViewById(R.id.playerCards);
    }

    private void SetListener() {
        moneyText.setOnClickListener(listener);
        betTextEdit.setOnClickListener(listener);
        playerScoreText.setOnClickListener(listener);
        dealerScoreText.setOnClickListener(listener);
        drawButton.setOnClickListener(listener);
        standButton.setOnClickListener(listener);
        dealerCardsLayout.setOnClickListener(listener);
        playerCardsLayout.setOnClickListener(listener);
    }
}