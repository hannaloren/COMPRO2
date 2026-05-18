package com.quizify.model;

public class Identification extends Question {

    public Identification(String prompt, String correctAnswer) {
        super(prompt, correctAnswer);
    }

    @Override
    public void display() {

        System.out.println();
        System.out.println("Question Type: Identification");
        System.out.println(prompt);
    }
}