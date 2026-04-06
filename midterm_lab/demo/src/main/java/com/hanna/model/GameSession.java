package com.hanna.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.hanna.Service.FileHandler;
import com.hanna.Service.Game;

public class GameSession {
    private final int logic = 10;
    private int round;
    private Player player1;
    private Player player2;
    private Socket c1, c2;

    public GameSession(Socket c1, Socket c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public GameSession(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
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

            Game game = new Game();

            ArrayList<Player> users = FileHandler.loadUsers();

            // ask the player to login
            String u1 = in1.readLine();
            String pw1 = in1.readLine();
            player1 = FileHandler.handleAuth(users, u1, pw1, out1);

            String u2 = in2.readLine();
            String pw2 = in2.readLine();
            player2 = FileHandler.handleAuth(users, u2, pw2, out2);

            if (player1 == null || player2 == null) {
                // Send a message so the other client doesn't hang
                if (out1 != null)
                    out1.println("Auth Failed");
                if (out2 != null)
                    out2.println("Auth Failed");
                return;
            }
            player1.setUsername(u1);
            player1.setPassword(pw1);
            player2.setPassword(pw2);
            player2.setUsername(u2);

            out1.println("MATCHED with " + player2.getUsername());
            out2.println("MATCHED with " + player1.getUsername());

            round = 1;
            for (; round <= logic; round++) {

                out1.println("ROUND " + round + ": Your move (ROCK/PAPER/SCISSORS): ");
                out2.println("ROUND " + round + ": Your move (ROCK/PAPER/SCISSORS): ");

                // read the moves from both players
                String input1 = in1.readLine();
                String input2 = in2.readLine();

                if (input1 == null || input2 == null) {
                    break;
                }

                // convert the input to GameMove objects
                GameMove m1 = GameMove.fromString(input1);
                GameMove m2 = GameMove.fromString(input2);

                if (m1 == null || m2 == null) {
                    out1.println("Invalid move!");
                    out1.println("You skipped this round.");
                    out1.println("--------------------");
                    out2.println("Invalid move!");
                    out2.println("You skipped this round.");
                    out2.println("--------------------");
                    continue;
                }

                player1.setMove(m1);
                player2.setMove(m2);
                String winner = game.determineWinner(player1, player2);

                if (!winner.equals("Draw!")) {
                    if (winner.equals(player1.getUsername())) {
                        player1.incrementScore();
                        player2.lose();
                    } else {
                        player2.incrementScore();
                        player1.lose();
                    }
                }

                // send the result to the terminal of both players
                out1.println("Winner: " + winner);
                out1.println("Your Winrate: " + player1.getScore() + "%");
                out1.println("---------------------");
                out2.println("Winner: " + winner);
                out2.println("Your Winrate: " + player2.getScore() + "%");
                out2.println("---------------------");

            }
            out1.println("Game Over! FINAL WINRATE SCORE: " + player1.getScore());
            out2.println("Game Over! FINAL WINRATE SCORE: " + player2.getScore());

            // will only be printes sa terminal ng server
            System.out.println("\n========== SCOREBOARD ==========");
            System.out.println("Player 1: " + player1.getUsername());
            System.out.println("Winrate   : " + player1.getScore() + "%");
            System.out.println("-------------------------------");
            System.out.println("Player 2: " + player2.getUsername());
            System.out.println("Winrate : " + player2.getScore() + "%");
            System.out.println("================================\n");

            // save the scores to the file
            for (Player p : users) {
                p.setMove(null);
            }

            FileHandler.saveUsers(users);
            FileHandler.saveUsers(users);
            c1.close();
            c2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
