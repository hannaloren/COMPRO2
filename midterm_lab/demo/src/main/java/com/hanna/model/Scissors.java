package com.hanna.model;

public class Scissors extends GameMove {
    public Scissors() {
        super("Scissors");

    }

    @Override
    public int compare(GameMove other) {
        if (other instanceof Scissors) {
            return 0; // Tie
        }
        if (other instanceof Paper) {
            return 1; // Scissors beats Paper
        }
        if (other instanceof Rock) {
            return -1; // Scissors loses to Rock
        }
        throw new IllegalArgumentException("Unknown move: " + other.getMoveName());
    }

}
