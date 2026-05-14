package com.example;

import java.util.Map;

public class Question {
    private String type;
    private String question;
    private Map<String, String> choices;
    private String answer;

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public Map<String, String> getChoices() {
        return choices;
    }

    public String getAnswer() {
        return answer;
    }
}