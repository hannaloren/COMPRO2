package com.quizify.client;

import java.util.Scanner;

public class AnswerThread extends Thread {

    private Scanner sc;

    public volatile String answer = "NO ANSWER";

    public AnswerThread(Scanner sc) {
        this.sc = sc;
    }

    @Override
    public void run() {
        answer = sc.nextLine();
    }
}