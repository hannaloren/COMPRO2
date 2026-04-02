package com.hanna.rpc;

import java.io.*;
import java.util.*;
import com.google.gson.*;

public class FileHandler {

    private static final String FILE = "C:\\Users\\Asus Vivobook\\COMPRO2\\rpc\\data\\userdata.json";

    public static ArrayList<Player> loadUsers() {
        ArrayList<Player> list = new ArrayList<>();

        try {
            File file = new File(FILE);
            if (!file.exists())
                return list;

            BufferedReader br = new BufferedReader(new FileReader(file));

            JsonArray arr = JsonParser.parseReader(br).getAsJsonArray();

            for (JsonElement e : arr) {
                JsonObject o = e.getAsJsonObject();

                list.add(new Player(
                        o.get("username").getAsString(),
                        o.get("password").getAsString(),
                        o.get("wins").getAsInt(),
                        o.get("losses").getAsInt()));
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void saveUsers(ArrayList<Player> users) {
        JsonArray arr = new JsonArray();

        for (Player p : users) {
            JsonObject o = new JsonObject();
            o.addProperty("username", p.getUsername());
            o.addProperty("password", p.getPassword());
            o.addProperty("wins", p.getWins());
            o.addProperty("losses", p.getLosses());
            arr.add(o);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            pw.write(gson.toJson(arr));
        } catch (Exception e) {
            e.printStackTrace();
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
}
