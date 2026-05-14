package com.quizify.model;


public class TrueFalse extends Question {

    public TrueFalse(String prompt, String correctAnswer) {
        super(prompt, correctAnswer);
    }

    @Override
    public void display() {
        System.out.println("[TRUE/FALSE] " + prompt);
        System.out.println("Enter T or F");
    }
}