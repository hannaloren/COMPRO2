package com.hanna.rpc;

public class Game {
    private String username;
    private String password;


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    private int wins;
    private int losses;
    private GameMove currentMove;

    public Game() {
    }

    public Game(String username, String password, int wins, int losses) {
        this.username = username;
        this.password = password;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String determineWinner(Player p1, Player p2) {
        int result = p1.getMove().compare(p2.getMove());
        if (result == 0)
            return "Draw!";
        if (result == 1)
            return p1.getUsername();

        return p2.getUsername();
    }
}
