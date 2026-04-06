package com.hanna.model;

public class Rock extends GameMove{
    public Rock() {
        super("0");
    }

    @Override
    public int compare(GameMove other) {
        if (other instanceof Rock) {
            return 0; // Tie
        }
        if (other instanceof Scissors) {
            return 1;
        } 
        if (other instanceof Paper) {
            return -1; 
        }
        throw new IllegalArgumentException("Unknown move: " + other.getMoveName());
    }
    
}
