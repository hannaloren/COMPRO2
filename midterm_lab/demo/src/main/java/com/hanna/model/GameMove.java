package com.hanna.model;

public abstract class GameMove {
    private String moveName;

    public String getMoveName() {
        return moveName;
    }

    public GameMove(String moveName) {
        this.moveName = moveName;
    }

    public abstract int compare(GameMove other);
}
