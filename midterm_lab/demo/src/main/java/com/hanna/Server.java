package com.hanna;

import com.hanna.model.*;
import com.hanna.Service.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    static List<Account> accs = new ArrayList<>();

    public static void main(String[] args) {
        int port = 8000;

        System.out.println("Waiting for players...");

        try (ServerSocket server = new ServerSocket(port)) {

            // Accept Player 1 and create streams
            Socket p1Socket = server.accept();
            PrintWriter out1 = new PrintWriter(p1Socket.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(p1Socket.getInputStream()));
            System.out.println("Player 1 connected!");

            // Accept Player 2 and create streams
            Socket p2Socket = server.accept();
            PrintWriter out2 = new PrintWriter(p2Socket.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(p2Socket.getInputStream()));
            System.out.println("Player 2 connected!");

            // Load accounts
            accs = FileHandler.loadAccounts("C:\\Users\\Asus Vivobook\\COMPRO2\\midterm_lab\\data\\accounts.json");
            Account a1 = ClientService.playerLogin(accs, in1, out1);

            // Make player 2 log in
            Account a2 = ClientService.playerLogin(accs, in2, out2);

            // Create Player objects
            Player p1 = new Player(a1.getUsername());
            Player p2 = new Player(a2.getUsername());

            // Create GameSession
            GameSession gs = new GameSession(p1, p2);

            // Notify both players game is starting
            out1.println("\nGame starting...");
            out2.println("\nGame starting...");

            // Game loop — 10 rounds
            for (int round = 1; round <= 10; round++) {

                // Announce round number to both players
                out1.println("\nRound " + round);
                out2.println("\nRound " + round);

                // Get GameMove from both players
                GameMove m1 = ClientService.handleChoice(in1, out1);
                GameMove m2 = ClientService.handleChoice(in2, out2);

                // Set moves on Player objects
                p1.setCurrentMove(m1);
                p2.setCurrentMove(m2);

                // Determine round winner and update scores
                GameResult result = gs.determineWinner();

                // Send result to both players
                out1.println(gs.formatResult(result));
                out2.println(gs.formatResult(result));

                // Show current scores
                out1.println("Score: " + p1.getName() + ": " + p1.getScore() + " \n" + p2.getName() + ": "
                        + p2.getScore());
                out2.println("Score: " + p1.getName() + ": " + p1.getScore() + " \n" + p2.getName() + ": "
                        + p2.getScore());

                // Reset moves for next round
                gs.resetRound();
            }

            // Update accounts based on session scores
            a1.addWins(p1.getScore());
            a1.addLoses(p1.getScore());
            a2.addWins(p2.getScore());
            a2.addLoses(p2.getScore());

            System.out.println("Match over! ");
            System.out.println(p1.getName() + " wins: " + p1.getScore());
            System.out.println(p2.getName() + " wins: " + p2.getScore());

            // Announce match over
            out1.println("\nMATCH OVER ");
            out2.println("\nMATCH OVER ");

            out1.println("WINNER: " + (p1.getScore() > p2.getScore() ? p1.getName() : p2.getName()) + "\nScore: "
                    + p1.getScore() + " - " + p2.getScore());
            out2.println("WINNER: " + (p1.getScore() > p2.getScore() ? p1.getName() : p2.getName()) + "\nScore: "
                    + p1.getScore() + " - " + p2.getScore());

            // Display leaderboard to both players
            ClientService.displayLeaderboard(accs, out1);
            ClientService.displayLeaderboard(accs, out2);

            // Save updated accounts to JSON
            FileHandler.saveAccounts("C:\\Users\\Asus Vivobook\\COMPRO2\\midterm_lab\\data\\accounts.json", accs);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
