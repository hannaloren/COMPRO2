package com.hanna.model;

public class GameSession {
    private Player player1;
    private Player player2;
    private int round;
    private static final int MAX_ROUNDS = 10;

  
    public GameSession(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.round = 0;
    }

    public int getRound() {
        return round;
    }

    public boolean isMatchOver() {
        return round == MAX_ROUNDS;
    }


    public void resetRound() {
        player1.resetMove();
        player2.resetMove();
        round++;
    }

    public GameResult determineWinner() {
        GameMove m1 = player1.getCurrentMove();
        GameMove m2 = player2.getCurrentMove();
        int result = m1.compare(m2);

        if (result == 0)
            return new GameResult("Draw", m1, m2);
        else if (result == 1) {
            player1.incrementScore();
            return new GameResult(player1.getName(), m1, m2);
        } else {
            player2.incrementScore();
            return new GameResult(player2.getName(), m1, m2);
        }
    }

    public String formatResult(GameResult result) {
        return result.toString();
    }
}
