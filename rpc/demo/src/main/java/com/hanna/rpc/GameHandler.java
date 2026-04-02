package com.hanna.rpc;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GameHandler {

    private Socket c1, c2;

    public GameHandler(Socket c1, Socket c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public void start() {
        run();
    }

    private void run() {
        try {
            BufferedReader in1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
            PrintWriter out1 = new PrintWriter(c1.getOutputStream(), true);

            BufferedReader in2 = new BufferedReader(new InputStreamReader(c2.getInputStream()));
            PrintWriter out2 = new PrintWriter(c2.getOutputStream(), true);

            Player p1 = new Player();
            Player p2 = new Player();
            Game game = new Result();

            // USERNAMES + PASSWORDS (FIXED FLOW)
            String u1 = in1.readLine();
            String pw1 = in1.readLine();

            String u2 = in2.readLine();
            String pw2 = in2.readLine();
            p1.setPassword(pw1);
            p2.setPassword(pw2);
            p1.setUsername(u1);
            p2.setUsername(u2);
            ArrayList<Player> users = FileHandler.loadUsers();

            out1.println("MATCHED with " + p2.getUsername());
            out2.println("MATCHED with " + p1.getUsername());

            int maxRounds = 10;
            int currentRound = 0;
            while (currentRound < maxRounds) {

                // PROMPT BOTH PLAYERS
                out1.println("ROUND " + (currentRound + 1) + ": Your move (ROCK/PAPER/SCISSORS): ");
                out2.println("ROUND " + (currentRound + 1) + ": Your move (ROCK/PAPER/SCISSORS): ");

                // RECEIVE INPUT
                String input1 = in1.readLine();
                String input2 = in2.readLine();

                if (input1 == null || input2 == null) {
                    break;
                }

                GameMove m1 = GameMove.fromString(input1);
                GameMove m2 = GameMove.fromString(input2);

                // VALIDATION
                if (m1 == null || m2 == null) {
                    out1.println("Invalid move! Try again.");
                    out2.println("Invalid move! Try again.");
                    continue;
                }

                p1.setMove(m1);
                p2.setMove(m2);

                String winner = game.determineWinner(p1, p2);

                if (!winner.equals("Draw!")) {
                    if (winner.equals(p1.getUsername())) {
                        p1.incrementScore();
                        p2.lose();
                    } else {
                        p2.incrementScore();
                        p1.lose();
                    }
                }

                // SEND RESULTS
                out1.println("Winner: " + winner);
                out2.println("Winner: " + winner);
                currentRound++;

            }
            out1.println("Game Over");
            out2.println("Game Over");

            System.out.println("\n========== SCOREBOARD ==========");
            System.out.println("Player 1: " + p1.getUsername());
            System.out.println("Score   : " + p1.getScore());
            System.out.println("-------------------------------");
            System.out.println("Player 2: " + p2.getUsername());
            System.out.println("Score   : " + p2.getScore());
            System.out.println("================================\n");

            users.add(p1);
            users.add(p2);
            FileHandler.saveUsers(users);
            c1.close();
            c2.close();

        } catch (

        Exception e) {
            e.printStackTrace();
        }

    }
}