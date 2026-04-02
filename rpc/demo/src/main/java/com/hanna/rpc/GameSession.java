package com.hanna.rpc;

public class GameSession {
    private final int logic = 10;
    private int round;
    private Player player1;
    private Player player2;
    public GameSession(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
      public boolean hasNextRound() {
        return round < logic;
    }

    public void nextRound() {
        round++;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}

    

