package com.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    private String playerName;

    private int score = 0;

    private volatile String latestAnswer = null;

    public ClientHandler(Socket socket) {

        this.socket = socket;

        try {

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            out.println("CLEAR");

            out.println("=================================");
            out.println("          QUIZIFY LOGIN");
            out.println("=================================");

            out.println("ENTER_NAME");
            String nameInput = in.readLine();

            out.println("ENTER_PASSWORD");
            String passwordInput = in.readLine();

            out.println("ENTER_CODE");
            String codeInput = in.readLine();

            if (nameInput == null ||
                    passwordInput == null ||
                    codeInput == null) {

                cleanup();
                return;
            }

            boolean verified = QuizServer.verifyStudent(
                    nameInput,
                    passwordInput,
                    codeInput);

            if (!verified) {

                out.println("VERIFICATION_FAILED");
                cleanup();
                return;
            }

            this.playerName = nameInput.trim();

            out.println("VERIFIED");

            System.out.println(
                    "Verified Student: "
                            + playerName);

            out.println("\nWELCOME " + playerName);
            out.println("Waiting for quiz to start...");

            while (!socket.isClosed()) {

                String msg = in.readLine();

                if (msg == null)
                    break;

                latestAnswer = msg.trim();
            }

        } catch (Exception e) {

            System.out.println(playerName + " disconnected.");

        } finally {

            cleanup();
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String getLatestAnswer() {
        return latestAnswer;
    }

    public void resetAnswer() {
        latestAnswer = null;
    }

    private void cleanup() {

        try {

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public void addScore() {
        score++;
    }
}