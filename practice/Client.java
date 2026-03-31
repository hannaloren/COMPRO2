package activity11.demo.rpc;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("192.168.110.181", 8000)) {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);

            String serverMsg;

            while ((serverMsg = in.readLine()) != null) {
                System.out.println(serverMsg);

                if (serverMsg.contains("Username") ||
                        serverMsg.contains("Password") ||
                        serverMsg.contains("Enter move")) {

                    String input = sc.nextLine();
                    out.println(input);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}