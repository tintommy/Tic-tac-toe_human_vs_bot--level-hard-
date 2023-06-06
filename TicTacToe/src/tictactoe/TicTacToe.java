package tictactoe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

class BestMove {

    public int row;
    public int col;
}

public class TicTacToe extends JFrame implements ActionListener {

    JLabel tittle = new JLabel();
    JPanel text_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JButton[][] button = new JButton[3][3];
    JButton reset = new JButton();
    Boolean xTurn;

    public TicTacToe() throws HeadlessException, InterruptedException {
        this.setSize(800, 800);
        this.getContentPane().setBackground(Color.black);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        this.setTitle("TIC-TAC-TOE");
        tittle.setForeground(Color.GREEN);
        tittle.setText("Tic Tac Toe");
        tittle.setFont(new Font("Ink Free", Font.BOLD, 80));

        tittle.setHorizontalAlignment(JLabel.CENTER);
        tittle.setBackground(Color.BLACK);
        tittle.setOpaque(true);
        text_panel.setLayout(new BorderLayout());
        text_panel.setBounds(0, 0, 800, 100);
        text_panel.add(tittle);
        reset.setText("RESET");
        reset.setSize(200, HEIGHT);
        reset.setFocusable(false);
        reset.addActionListener(this);
        text_panel.add(reset, BorderLayout.EAST);
        button_panel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                button[i][j] = new JButton();
                button_panel.add(button[i][j]);
                button[i][j].addActionListener(this);
                button[i][j].setFont(new Font("MV Boli", Font.BOLD, 150));
                button[i][j].setBackground(new Color(0, 51, 128));
                button[i][j].setFocusable(false);
                button[i][j].setBorder(BorderFactory.createLineBorder(Color.white));
            }
        }
        this.add(text_panel, BorderLayout.NORTH);
        this.add(button_panel);
        Thread.sleep(1000);
        firstTurn();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (e.getSource() == button[i][j]) {

                    if (button[i][j].getText() == "" && xTurn == true) {
                        button[i][j].setForeground(Color.white);
                        button[i][j].setText("X");
                        if (checkWin() == 1) {
                            tittle.setText("X Win");
                        } else {
                            try {
                                findBestMove();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            xTurn = Boolean.TRUE;
                            tittle.setForeground(Color.WHITE);
                            tittle.setText("X Turn");

                            if (checkWin() == 2) {
                                tittle.setForeground(Color.red);
                                tittle.setText("O Win");
                            }
                        }

                    }
                }

            }
        }

        if (e.getSource() == reset) {
            tittle.setText("Tic Tac Toe");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    button[i][j].setText("");
                    button[i][j].setBackground(new Color(0, 51, 128));
                    button[i][j].setEnabled(true);
                }
            }
            try {
                firstTurn();
            } catch (InterruptedException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    void firstTurn() throws InterruptedException {

        Random random = new Random();
        int a = random.nextInt(3);
        if (a == 0) {
            xTurn = false;
            tittle.setForeground(Color.red);
            tittle.setText("O Turn");
            findBestMove();
            tittle.setForeground(Color.WHITE);
            tittle.setText("X Turn");
        } else {
            tittle.setForeground(Color.WHITE);
            tittle.setText("X Turn");
            xTurn = true;
        }
    }

    public int minimax(int depth, boolean isMax, int alpha, int beta) {
        int score = evaluate();

        if (score == 10) {
            return score - depth;
        }
        if (score == -10) {
            return score + depth;
        }
        if (isBoardFull()) {
            return 0;
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (button[i][j].getText() == "") {
                        button[i][j].setText("O");
                        int val = minimax(depth + 1, !isMax, alpha, beta);
                        best = Math.max(best, val);
                        alpha = Math.max(alpha, best);
                        button[i][j].setText("");
                        if (beta <= alpha) {
                            break;
                        }

                    }
                }
            }

            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (button[i][j].getText() == "") {
                        button[i][j].setText("X");
                        int val = minimax(depth + 1, !isMax, alpha, beta);
                        best = Math.min(best, val);
                        beta = Math.min(beta, best);
                        button[i][j].setText("");
                        if (beta <= alpha) {
                            break;
                        }

                    }
                }
            }
            return best;
        }
    }

    public void findBestMove() throws InterruptedException {

        int bestVal = Integer.MIN_VALUE;
        BestMove bestMove = new BestMove();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (button[i][j].getText() == "") {
                    button[i][j].setText("O");
                    int moveVal = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    button[i][j].setText("");

                    if (moveVal > bestVal) {
                        bestVal = moveVal;
                        bestMove.row = i;
                        bestMove.col = j;
                    }
                }
            }
        }
    
        button[bestMove.row][bestMove.col].setForeground(Color.RED);
        button[bestMove.row][bestMove.col].setText("O");
        xTurn = true;
    }

    private int evaluate() {
        for (int row = 0; row < 3; row++) {
            if (button[row][0].getText().equals(button[row][1].getText()) && button[row][1].getText().equals(button[row][2].getText())) {
                if (button[row][0].getText().equals("O")) {
                    return 10;
                } else if (button[row][0].getText().equals("X")) {
                    return -10;
                }
            }
        }

        for (int col = 0; col < 3; col++) {
            if (button[0][col].getText().equals(button[1][col].getText()) && button[1][col].getText().equals(button[2][col].getText())) {
                if (button[0][col].getText().equals("O")) {
                    return 10;
                } else if (button[0][col].getText().equals("X")) {
                    return -10;
                }
            }
        }

        if (button[0][0].getText().equals(button[1][1].getText()) && button[1][1].getText().equals(button[2][2].getText())) {
            if (button[0][0].getText().equals("O")) {
                return 10;
            } else if (button[0][0].getText().equals("X")) {
                return -10;
            }
        }

        if (button[0][2].getText().equals(button[1][1].getText()) && button[1][1].getText().equals(button[2][0].getText())) {
            if (button[0][2].getText().equals("O")) {
                return 10;
            } else if (button[0][2].getText().equals("X")) {
                return -10;
            }
        }

        return 0;
    }

    public int checkWin() {
        for (int row = 0; row < 3; row++) {
            if (button[row][0].getText().equals(button[row][1].getText()) && button[row][1].getText().equals(button[row][2].getText())) {

                if (button[row][0].getText().equals("O")) {

                    win(row, 0, row, 1, row, 2);
                    setOffButton();
                    return 2;
                } else if (button[row][0].getText().equals("X")) {
                    win(row, 0, row, 1, row, 2);
                    setOffButton();
                    return 1;
                }
            }
        }

        for (int col = 0; col < 3; col++) {
            if (button[0][col].getText().equals(button[1][col].getText()) && button[1][col].getText().equals(button[2][col].getText())) {

                if (button[0][col].getText().equals("O")) {
                    win(0, col, 1, col, 2, col);
                    setOffButton();
                    return 2;
                } else if (button[0][col].getText().equals("X")) {
                    win(0, col, 1, col, 2, col);
                    setOffButton();
                    return 1;
                }
            }
        }

        if (button[0][0].getText().equals(button[1][1].getText()) && button[1][1].getText().equals(button[2][2].getText())) {

            if (button[0][0].getText().equals("O")) {
                win(0, 0, 1, 1, 2, 2);
                setOffButton();
                return 2;
            } else if (button[0][0].getText().equals("X")) {
                win(0, 0, 1, 1, 2, 2);
                setOffButton();
                return 1;
            }
        }

        if (button[0][2].getText().equals(button[1][1].getText()) && button[1][1].getText().equals(button[2][0].getText())) {

            if (button[0][2].getText().equals("O")) {
                win(0, 2, 1, 1, 2, 0);
                setOffButton();
                return 2;
            } else if (button[0][2].getText().equals("X")) {
                win(0, 2, 1, 1, 2, 0);
                setOffButton();
                return 1;
            }
        }
        if (isBoardFull() == true) {
            tittle.setForeground(Color.YELLOW);
            tittle.setText("DRAW");
        }
        return 0;
    }

    void setOffButton() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                button[i][j].setEnabled(false);
            }
        }

    }

    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (button[i][j].getText() == "") {
                    return false;
                }
            }
        }
        return true;

    }

    public void win(int a, int b, int c, int d, int e, int f) {
        button[a][b].setBackground(new Color(0, 255, 0));
        button[c][d].setBackground(new Color(0, 255, 0));
        button[e][f].setBackground(new Color(0, 255, 0));

    }

    public static void main(String[] args) throws HeadlessException, InterruptedException {
        TicTacToe t = new TicTacToe();
    }
}
