package com.hanna.model;

public class Paper extends GameMove {
    public Paper() {
        super("Paper");
    }

    @Override
    public int compare(GameMove other) {
        if (other instanceof Paper) {
            return 0; // Tie
        }
        if (other instanceof Rock) {
            return 1;
        }
        if (other instanceof Scissors) {
            return -1;
        }
        throw new IllegalArgumentException("Unknown move: " + other.getMoveName());
    }

}
