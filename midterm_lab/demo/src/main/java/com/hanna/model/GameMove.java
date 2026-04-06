package com.hanna.model;

public abstract class GameMove {
    private String moveName;

    public String getMoveName() {
        return moveName;
    }

    public GameMove(String moveName) {
        this.moveName = moveName;
    }

    public static GameMove fromString(String move) {
        switch (move.toLowerCase()) {
            case "0":
                return new Rock();
            case "1":
                return new Paper();
            case "2":
                return new Scissors();
            default:
                throw new IllegalArgumentException("Invalid move: " + move);
        }
    }

    public abstract int compare(GameMove other);
}
