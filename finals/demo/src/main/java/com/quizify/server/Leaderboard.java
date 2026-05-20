package com.quizify.server;

import java.util.List;

public class Leaderboard {

    public static synchronized void show(List<ClientHandler> clients) {

        clients.sort((a, b) -> b.score - a.score);

        System.out.println();
        System.out.println("====================================");
        System.out.println("           LIVE LEADERBOARD");
        System.out.println("====================================");

        int rank = 1;

        for (ClientHandler c : clients) {

            System.out.printf(
                    "%d. %-15s Score: %d%n",
                    rank,
                    c.username,
                    c.score);

            rank++;
        }

        System.out.println("====================================");
    }
}