package com.hanna.rpc;

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
            case "rock":
                return new Rock();
            case "paper":
                return new Paper();
            case "scissors":
                return new Scissors();
            default:
                throw new IllegalArgumentException("Invalid move: " + move);
        }
    }

    public abstract int compare(GameMove other);
}
