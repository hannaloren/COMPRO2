package com.hanna.model;

public class Player {
    private String name;
    private int score;
    private GameMove currentMove;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.currentMove = null;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public GameMove getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(GameMove move) {
        this.currentMove = move;
    }

    public void resetMove() {
        this.currentMove = null;
    }

    public void incrementScore() {
        score++;
    }
}
