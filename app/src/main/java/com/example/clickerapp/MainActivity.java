package com.example.clickerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public ImageButton midasButton;
    public TextView scoreTextView;
    public Button changeThemes;
    public Button tictacbuton;

    public int ClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ClickCount = getSharedPreferences("Clicks", MODE_PRIVATE)
                .getInt("clickCount", 0);

        if (savedInstanceState != null) {
            ClickCount = savedInstanceState.getInt("clickCount", 0);
        }

        midasButton = findViewById(R.id.MidasButton);
        scoreTextView = findViewById(R.id.ScoreTextView);
        changeThemes = findViewById(R.id.ChangeThemes);
        tictacbuton = findViewById(R.id.TicTacButton);

        scoreTextView.setText(String.valueOf(ClickCount));

        changeThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });

        midasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickCount++;
                if (ClickCount % 10 == 0) {
                    Toast.makeText(MainActivity.this, "ЛИСТИНГА НЕ БУДЕТ!", Toast.LENGTH_SHORT).show();
                }
                scoreTextView.setText(String.valueOf(ClickCount));
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.soundmidas);
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
            }
        });
        tictacbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("Clicks", MODE_PRIVATE)
                        .edit()
                        .putInt("clickCount", ClickCount)
                        .apply();
                Intent intent = new Intent(MainActivity.this, TicTacToeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("clickCount", ClickCount);
    }
}
