package com.hanna.rpc;

public class Scissors extends GameMove {
    public Scissors() {
        super("Paper");

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
