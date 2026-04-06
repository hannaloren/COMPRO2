package com.hanna.model;

public class Player {
    private String username;
    private String password;
    private int wins;
    private int losses;
    private int score;
    private GameMove currentMove;

    public Player() {

    }

    public Player(String username, String password, int wins, int losses) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
    }

    public Player(String username, String password, int wins, int losses, int score, GameMove currentMove) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
        this.score = score;
        this.currentMove = currentMove;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
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

    public int getScore() {
        int totalGames = wins + losses;
        if (totalGames == 0) {
            score = 0;
        } else {
            score = (int) ((double) wins / totalGames * 100);
        }
        return score;
    }

    public int setWins(int wins) {
        this.wins = wins;
        return wins;
    }

    public int setLosses(int losses) {
        this.losses = losses;
        return losses;
    }

    public int setScore(int score) {
        this.score = score;
        return score;

    }

}
