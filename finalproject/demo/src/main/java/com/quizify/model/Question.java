package com.quizify.model;

import java.io.Serializable;

public abstract class Question implements Serializable {
    protected String prompt;
    protected String correctAnswer;

    public Question(String prompt, String correctAnswer) {
        this.prompt = prompt;
        this.correctAnswer = correctAnswer;
    }

    public abstract void display();

    public boolean checkAnswer(String answer) {
        return correctAnswer.equalsIgnoreCase(answer.trim());
    }

    public String getPrompt() {
        return prompt;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}