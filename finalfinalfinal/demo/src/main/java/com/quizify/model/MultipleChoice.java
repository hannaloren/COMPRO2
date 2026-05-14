package com.quizify.model;


public class MultipleChoice extends Question {

    private String[] options;

    public MultipleChoice(String prompt, String[] options, String correctAnswer) {
        super(prompt, correctAnswer);
        this.options = options;
    }

    @Override
    public void display() {
        System.out.println("[MCQ] " + prompt);
        for (int i = 0; i < options.length; i++) {
            System.out.println((char)('A' + i) + ") " + options[i]);
        }
    }
}