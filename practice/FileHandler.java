package activity11.demo.rpc;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private static final String FILE = "C:\\Users\\STUDENTS\\COMPRO2\\activity11\\data\\userdata.json";

    public static ArrayList<Player> loadUsers() {
        ArrayList<Player> list = new ArrayList<>();

        try {
            File file = new File(FILE);
            if (!file.exists())
                return list;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String json = "";
            String line;

            while ((line = br.readLine()) != null)
                json += line;

            JSONArray arr = new JSONArray(json);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);

                list.add(new Player(
                        o.getString("username"),
                        o.getString("password"),
                        o.getInt("wins"),
                        o.getInt("losses")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void saveUsers(ArrayList<Player> users) {
        JSONArray arr = new JSONArray();

        for (Player p : users) {
            JSONObject o = new JSONObject();
            o.put("username", p.getUsername());
            o.put("password", p.getPassword());
            o.put("wins", p.getWins());
            o.put("losses", p.getLosses());
            arr.put(o);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.write(arr.toString(4));
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
