package com.example;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String name;
    private String code;

    private int score = 0;

    private volatile String answer = null;
    private volatile boolean canAnswer = false;

    private long answerDeadline = 0;

    public ClientHandler(Socket socket) {

        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            out.println("ENTER_NAME");
            String n = in.readLine();

            out.println("ENTER_PASSWORD");
            String p = in.readLine();

            out.println("ENTER_CODE");
            String c = in.readLine();

            this.code = c;

            if (!QuizServer.verifyStudent(n, p, c, this)) {
                out.println("FAILED");
                socket.close();
                return;
            }

            this.name = n;

            out.println("VERIFIED");

            while (!socket.isClosed()) {

                String msg = in.readLine();
                if (msg == null)
                    break;

                if (msg.equals("PING")) {
                    QuizServer.updateHeartbeat(code);
                    continue;
                }

                if (canAnswer) {

                    if (System.currentTimeMillis() > answerDeadline) {
                        QuizServer.reportCheater(name, "Late Answer Attempt");
                        continue;
                    }

                    if (answer == null) {
                        answer = msg.trim();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(name + " disconnected");
        } finally {
            cleanup();
        }
    }

    public void enableAnswering() {
        canAnswer = true;
        answerDeadline = System.currentTimeMillis() + 30000;
    }

    public void disableAnswering() {
        canAnswer = false;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String getLatestAnswer() {
        return answer;
    }

    public void resetAnswer() {
        answer = null;
    }

    public String getPlayerName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addScore() {
        score++;
    }

    private void cleanup() {
        try {
            if (code != null)
                QuizServer.removeUser(code);
            socket.close();
        } catch (Exception ignored) {
        }
    }
}