package com.hanna.model;

public class GameResult {
    private String winner;
    private GameMove p1Move;
    private GameMove p2Move;

    public GameResult(String winner, GameMove p1Move, GameMove p2Move) {
        this.winner = winner;
        this.p1Move = p1Move;
        this.p2Move = p2Move;
    }

    @Override
    public String toString() {
        if (winner.equals("Draw"))
            return " It's a draw!";
        return winner + " wins!";
    }

}
