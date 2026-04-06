package com.hanna.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hanna.model.Player;

public class FileHandler {

    private static final String FILE = "C:\\Users\\Asus Vivobook\\COMPRO2\\rpc\\data\\userdata.json";

    public static ArrayList<Player> loadUsers() {
        ArrayList<Player> list = new ArrayList<>();

        try {
            File file = new File(FILE);
            if (!file.exists())
                return list;

            BufferedReader br = new BufferedReader(new FileReader(file));

            Gson gson = new Gson();

            list = gson.fromJson(br, new TypeToken<ArrayList<Player>>() {
            }.getType());

            br.close();

            if (list == null)
                list = new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void saveUsers(ArrayList<Player> users) {

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(users);
            pw.write(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePlayerStats(ArrayList<Player> users, Player currentPlayer) {
        boolean found = false;
        for (Player u : users) {
            if (u.getUsername().equalsIgnoreCase(currentPlayer.getUsername())) {
                // Update existing player stats
                u.setWins(u.getWins() + currentPlayer.getWins());
                u.setLosses(u.getLosses() + currentPlayer.getLosses());
                u.setScore(currentPlayer.getScore()); // Update to latest score
                return;
            }
        }
        if (!found) {
            // If brand new player, add them to the array
            users.add(currentPlayer);
        }
    }

    public static Player login(ArrayList<Player> users, String u, String p) {
        for (Player player : users) {
            if (player.getUsername().equals(u) && player.getPassword().equals(p)) {
                return player;
            }
        }
        return null;
    }

    public static Player handleAuth(ArrayList<Player> users, String username, String password, PrintWriter out) {
        for (Player p : users) {
            if (p.getUsername().equalsIgnoreCase(username)) {
                // if user exist check password
                if (p.getPassword().equals(password)) {
                    return p;
                } else {
                    out.println("WRONG PASSWORD! Connection closing.");
                    return null;
                }
            }
        }

        Player newPlayer = new Player(username, password, 0, 0);
        users.add(newPlayer);
        out.println("Account created successfully!");
        return newPlayer;
    }
}