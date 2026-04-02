package com.hanna.rpc;

public class Paper extends GameMove{
    public Paper() {
        super("Paper");
    }

    @Override
    public int compare(GameMove other) {
        if (other instanceof Paper) {
            return 0; // Tie
        } 
        if (other instanceof Rock) {
            return 1; // Paper beats Rock
        } 
        if (other instanceof Scissors) {
            return -1; // Paper loses to Scissors
        }
        throw new IllegalArgumentException("Unknown move: " + other.getMoveName());
    }

    
}
