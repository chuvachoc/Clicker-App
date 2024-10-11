package com.example.clickerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TicTacToeActivity extends AppCompatActivity {

    private char currentPlayer = 'X';
    private char[][] board = new char[3][3];
    private GridLayout gridLayout;
    private int winsX, winsO, draw;
    private TextView winsXTextView, winsOTextView, drawTextView;
    public  Button clickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictactoe);

        gridLayout = findViewById(R.id.gridLayout);
        winsXTextView = findViewById(R.id.winsXTextView);
        winsOTextView = findViewById(R.id.winsOTextView);
        drawTextView = findViewById(R.id.drawTextView);
        clickerButton = findViewById(R.id.Clicker);


        clickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            initializeBoard();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("winsX", winsX);
        editor.putInt("winsO", winsO);
        editor.putInt("draw", draw);
        editor.putString("currentPlayer", String.valueOf(currentPlayer));

        StringBuilder boardState = new StringBuilder();
        for (char[] row : board) {
            boardState.append(row);
        }
        editor.putString("board", boardState.toString());
        editor.apply();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        winsX = preferences.getInt("winsX", 0);
        winsO = preferences.getInt("winsO", 0);
        draw = preferences.getInt("draw", 0);
        currentPlayer = preferences.getString("currentPlayer", "X").charAt(0);

        String boardState = preferences.getString("board", "");
        if (boardState.length() == 9) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = boardState.charAt(i * 3 + j);
                }
            }
        }
        updateBoardFromState();
    }


    private void initializeBoard() {
        gridLayout.removeAllViews();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button(this);
                button.setText("");
                button.setOnClickListener(new ButtonClickListener(i, j));
                button.setTextSize(40);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 250;
                params.height = 250;
                button.setLayoutParams(params);
                gridLayout.addView(button);
            }
        }
    }

    private void updateBoardFromState() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) gridLayout.getChildAt(i * 3 + j);
                button.setText(String.valueOf(board[i][j]));
            }
        }
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        winsXTextView.setText("Побед у крестиков: " + winsX);
        winsOTextView.setText("Побед у ноликов: " + winsO);
        drawTextView.setText("Ничей: " + draw);
    }

    private void restartGame() {
        board = new char[3][3];
        currentPlayer = 'X';
        initializeBoard();
        updateScoreDisplay();
    }

    private class ButtonClickListener implements View.OnClickListener {
        private int row;
        private int col;

        ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View view) {
            if (board[row][col] == 0) {
                ((Button) view).setText(String.valueOf(currentPlayer));
                board[row][col] = currentPlayer;
                if (checkWinner() || checkDraw()) {
                    String winner = checkWinner() ? String.valueOf(currentPlayer) : "Ничья";
                    if ("X".equals(winner)) winsX++;
                    else if ("O".equals(winner)) winsO++;
                    else draw++;
                    Toast.makeText(TicTacToeActivity.this, winner.equals("Ничья") ? winner : "Победили " + winner, Toast.LENGTH_SHORT).show();
                    restartGame();
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            }
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) return true;
        }
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == currentPlayer && board[1][j] == currentPlayer && board[2][j] == currentPlayer) return true;
        }
        return (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) ||
                (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer);
    }

    private boolean checkDraw() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }
}
