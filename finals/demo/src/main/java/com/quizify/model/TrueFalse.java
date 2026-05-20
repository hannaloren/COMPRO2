package com.quizify.model;

public class TrueFalse extends Question {

    public TrueFalse(String prompt, String correctAnswer) {
        super(prompt, correctAnswer);
    }

    @Override
    public void display() {
        System.out.println("Question Type: T/F");
        System.out.println(prompt);
        System.out.println("Enter T or F");
    }
}