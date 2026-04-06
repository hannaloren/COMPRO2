package com.hanna.Service;

import com.hanna.model.GameMove;
import com.hanna.model.Player;

public class Game {
    private int wins;
    private int losses;
    private GameMove currentMove;

    public Game() {

    }

    public Game(String username, String password, int wins, int losses) {
        this.wins = wins;
        this.losses = losses;
    }

    public void incrementScore() {
        wins++;
    }

    public void lose() {
        losses++;
    }

    public GameMove getMove() {
        return currentMove;
    }

    public void setMove(GameMove move) {
        this.currentMove = move;
    }

    public String getScore() {
        int total = wins + losses;
        double rate = total == 0 ? 0 : ((double) wins / total) * 100;
        return String.format("%.2f%%", rate);
    }

    public static String determineWinner(Player p1, Player p2) {
        int result = p1.getMove().compare(p2.getMove());
        if (result == 0)
            return "Draw!";
        if (result == 1)
            return p1.getUsername();

        return p2.getUsername();
    }
}
