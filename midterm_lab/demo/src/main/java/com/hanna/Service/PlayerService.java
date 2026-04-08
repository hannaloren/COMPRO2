package com.hanna.Service;

import com.hanna.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PlayerService {
    public static Account playerLogin(List<Account> accounts, BufferedReader in, PrintWriter out) throws IOException {
        out.println("""
                \nWelcome to RPC Game!
                1. Sign in
                2. Sign up
                """);

        Account a = null;
        boolean val = false;

        while (!val) {
            out.println("Choice: ");
            int choice1;
            try {
                choice1 = Integer.parseInt(in.readLine());
            } catch (NumberFormatException e) {
                out.println("Invalid input. Enter 1 or 2.");
                continue;
            }

            if (choice1 == 1) {
                while (true) {
                    String name = getPlayerName(in, out).trim();
                    if (name.isEmpty()) {
                        out.println("Name cannot be empty.");
                        continue;
                    }
                    a = findPlayer(accounts, name);
                    if (a != null) {
                        // Password loop — stays here until correct
                        while (true) {
                            out.println("\nEnter password: ");
                            String pw = in.readLine();
                            if (!pw.equals(a.getPassword())) {
                                out.println("Incorrect password! Try again.");
                                continue; // only re-asks password!
                            }
                            out.println("\nWelcome back, " + a.getUsername() + "!");
                            out.println(a.getSummary());
                            out.println("\nWaiting for another player...");
                            val = true;
                            break;
                        }
                        break;
                    } else {
                        out.println("Player not found. Try again.");
                    }
                }
            } else if (choice1 == 2) {
                while (true) {
                    String name = getPlayerName(in, out).trim();
                    if (name.isEmpty()) {
                        out.println("Name cannot be empty.");
                        continue;
                    }
                    a = findPlayer(accounts, name);
                    if (a == null) {
                        out.println("\nSet password: ");
                        String pw = in.readLine();
                        if (pw.isEmpty()) {
                            out.println("Password cannot be empty.");
                            continue;
                        }
                        out.println("\nCreating new account...");
                        a = new Account(name, pw);
                        accounts.add(a);
                        out.println("\nWelcome to the game, " + a.getUsername() + "!");
                        out.println("\nWaiting for another player...");
                        val = true;
                        break;
                    } else {
                        out.println("The name already exists. Try another.");
                    }
                }
            } else {
                out.println("Invalid input.");
            }
        }
        return a;
    }

    public static String getPlayerName(BufferedReader in, PrintWriter out) throws IOException {
        out.println("\nPlayer Name: ");
        return in.readLine();
    }

    public static Account findPlayer(List<Account> accounts, String name) {
        for (Account a : accounts) {
            if (a.getUsername().equalsIgnoreCase(name))
                return a;
        }
        return null;
    }

    public static GameMove handleChoice(BufferedReader in, PrintWriter out) throws IOException {
        while (true) {
            out.println("YOUR MOVE (0=Rock, 1=Paper, 2=Scissors): ");
            try {
                int choice = Integer.parseInt(in.readLine());
                if (choice == 0)
                    return new Rock();
                if (choice == 1)
                    return new Paper();
                if (choice == 2)
                    return new Scissors();
                out.println("Invalid input!");
            } catch (NumberFormatException e) {
                out.println("Invalid input!");
            }
        }
    }

    public static void displayLeaderboard(List<Account> accounts, PrintWriter out) {
        out.println("\nLEADERBOARD ");

        for (int i = 0; i < accounts.size() - 1; i++) {
            for (int j = 0; j < accounts.size() - i - 1; j++) {
                if (accounts.get(j).getWinRate() < accounts.get(j + 1).getWinRate()) {
                    Account temp = accounts.get(j);
                    accounts.set(j, accounts.get(j + 1));
                    accounts.set(j + 1, temp);
                }
            }
        }

        for (Account a : accounts) {
            out.println(a.getUsername() + " - " + a.getSummary());
        }
    }
}
