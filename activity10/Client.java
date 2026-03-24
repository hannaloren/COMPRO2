package activity10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String server = "192.168.110.204"; // same as 127.0.0.1
        int port = 8000;
        try (Socket socket = new Socket(server, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner sc = new Scanner(System.in);) {

            System.out.println("Connected to the server. Welcome");
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);
                if (serverMessage.contains("Enter") || serverMessage.contains("Guess") || serverMessage.contains("?")) {
                    String input = sc.nextLine();
                    out.println(input);

                    String reply = in.readLine();
                    if (reply == null || reply.equalsIgnoreCase("/quit")) {
                        System.out.println("Server disconnected...");
                        break;
                    }

                    System.out.println(reply);
                }
            }

        } catch (IOException e) {
            System.out.println("Can't connect right now...");
        }

    }
}
