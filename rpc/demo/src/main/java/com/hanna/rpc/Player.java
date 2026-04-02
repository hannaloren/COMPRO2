package com.hanna.rpc;

public class Player {
    private String username;
    private String password;
    private int wins;
    private int losses;
    private int score;
    private GameMove currentMove;

    public void setScore(int wins, int losses) {
        this.wins = wins;
        this.losses = losses;
        this.score = wins - losses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getScore() {
        int total = wins + losses;
        double rate = total == 0 ? 0 : ((double) wins / total) * 100;
        return String.format("%.2f", rate);
    }

}
