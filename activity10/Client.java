package activity10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String server = "192.168.1.87"; 
        int port = 8000;

        try (
            Socket socket = new Socket(server, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server!");

            String serverMessage;
            // Read messages from server continuously
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);

                // if the message looks like a prompt, get user input
                if (needsInput(serverMessage)) {
                    System.out.print("> "); 
                    String input = sc.nextLine();
                    out.println(input);
                }
            }
            System.out.println("Server disconnected.");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static boolean needsInput(String msg) {
        String lower = msg.toLowerCase();
        return lower.contains("choice:") || 
               lower.contains("username:") || 
               lower.contains("password:") || 
               lower.contains("guess the word:");
    }
}