package com.hanna.model;

public class Account {
    private String username;
    private String password;
    private int wins;
    private int losses;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.wins = 0;
        this.losses = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }

    public void addWins(int amount) {
        wins += amount;
    }

    public void addLoses(int amount) {
        losses += amount;
    }

    public double getWinRate() {
        if (wins + losses == 0)
            return 0;
        return (double) wins / (wins + losses) * 100;
    }

    public String getSummary() {
        return String.format("Wins: %d  Losses: %d  Win Rate: %.1f%%", wins, losses, getWinRate());
    }

}
