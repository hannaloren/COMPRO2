package activity11.demo.rpc;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static void main(String[] args) {
        int port = 8000;

        ArrayList<Player> users = UserDataManager.loadUsers();

        try (ServerSocket server = new ServerSocket(port)) {

            System.out.println("Waiting for players...");

            Socket c1 = server.accept();
            Socket c2 = server.accept();

            BufferedReader in1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
            PrintWriter out1 = new PrintWriter(c1.getOutputStream(), true);

            BufferedReader in2 = new BufferedReader(new InputStreamReader(c2.getInputStream()));
            PrintWriter out2 = new PrintWriter(c2.getOutputStream(), true);

            Player p1 = login(out1, in1, users);
            Player p2 = login(out2, in2, users);

            out1.println("Enter move:");
            p1.setMove(Move.fromString(in1.readLine()));

            out2.println("Enter move:");
            p2.setMove(Move.fromString(in2.readLine()));

            Game game = new RPSGame();
            String winner = game.determineWinner(p1, p2);

            if (winner.equals("Draw!")) {
                out1.println("Draw!");
                out2.println("Draw!");
            } else {
                if (winner.equals(p1.getUsername())) {
                    p1.win();
                    p2.lose();
                } else {
                    p2.win();
                    p1.lose();
                }

                out1.println("Winner: " + winner);
                out2.println("Winner: " + winner);
            }

            UserDataManager.saveUsers(users);

            c1.close();
            c2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Player login(PrintWriter out, BufferedReader in, ArrayList<Player> users) throws IOException {
        while (true) {
            out.println("Username:");
            String u = in.readLine();

            out.println("Password:");
            String p = in.readLine();

            Player player = UserDataManager.login(users, u, p);

            if (player != null) {
                out.println("Login successful!");
                return player;
            } else {
                out.println("Invalid. Try again.");
            }
        }
    }
}