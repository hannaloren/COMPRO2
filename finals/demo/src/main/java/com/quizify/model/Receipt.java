package com.quizify.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Receipt implements Serializable {

    private ArrayList<String> logs = new ArrayList<>();

    public void add(String text) {
        logs.add(text);
    }

    public void print(String username, int score) {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "MM/dd/yyyy HH:mm:ss");

        String formattedDateTime = now.format(formatter);

        System.out.println();
        System.out.println("====================================");
        System.out.println("         QUIZIFY STUDENT DATA");
        System.out.println("====================================");

        System.out.println(
                "Student: " + username);

        System.out.println(
                "Score: "
                        + score);

        System.out.println(
                "Generated: "
                        + formattedDateTime);

        System.out.println("====================================");

        for (String log : logs) {

            System.out.println(log);

        }

        System.out.println("====================================");
        System.out.println("         END OF STUDENT DATA");
        System.out.println("====================================");
    }
}