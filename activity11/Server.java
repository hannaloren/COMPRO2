package activity11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        int maxPlayers = 2;
        String playerNames[] = new String[maxPlayers];
        String playerPassword[] = new String[maxPlayers];
        ArrayList<String> moves = new ArrayList<>();
        int playerCount = 0;
        int port = 8000;

        try (ServerSocket server = new ServerSocket(port);
                Scanner sc = new Scanner(System.in);) {
            System.out.println("Waiting for client to be connected...");
            Socket client1 = server.accept();
            System.out.println("Client 1 is connected...");
            Socket client2 = server.accept();
            System.out.println("Client 2 is connected...");

            PrintWriter out = new PrintWriter(client1.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client1.getInputStream()));
            while (true) {
                do {
                    for (int i = 0; i <= 2; i++) {
                        out.println("Enter username:");
                        String name = in.readLine(); // get from client
                        playerNames[playerCount] = name;

                        out.println("Enter password: ");
                        String password = in.readLine();
                        playerPassword[playerCount] = password;
                    }

                } while (true);
            }
        } catch (IOException e) {

        }
    }
}
