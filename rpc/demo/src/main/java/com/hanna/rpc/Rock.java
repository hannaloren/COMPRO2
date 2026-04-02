package com.hanna.rpc;

public class Rock extends GameMove{
    public Rock() {
        super("Rock");
    }

    @Override
    public int compare(GameMove other) {
        if (other instanceof Rock) {
            return 0; // Tie
        }
        if (other instanceof Scissors) {
            return 1; // Rock beats Scissors
        } 
        if (other instanceof Paper) {
            return -1; // Rock loses to Paper
        }
        throw new IllegalArgumentException("Unknown move: " + other.getMoveName());
    }
    
}
