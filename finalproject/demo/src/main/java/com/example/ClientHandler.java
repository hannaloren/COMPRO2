package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String name;
    private String code;
    private final AtomicInteger score = new AtomicInteger(0);
    private volatile String answer = null;
    private volatile boolean canAnswer = false;
    private long answerDeadline = 0;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
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

            if (n == null || p == null || c == null)
                return;
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

                if (msg.equalsIgnoreCase("PING")) {
                    QuizServer.updateHeartbeat(code);
                    continue;
                }

                if (canAnswer) {
                    // Added 2-second grace period for network latency
                    if (System.currentTimeMillis() > (answerDeadline + 2000)) {
                        QuizServer.reportCheater(name, "Late Answer Attempt");
                        continue;
                    }
                    if (answer == null) {
                        answer = msg.trim();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println((name != null ? name : "Unknown") + " disconnected");
        } finally {
            cleanup();
        }
    }

    public void enableAnswering() {
        answer = null; // Reset buffer for new question
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
        return score.get();
    }

    public void addScore() {
        score.incrementAndGet();
    }

    private void cleanup() {
        try {
            if (code != null)
                QuizServer.removeUser(code);
            if (!socket.isClosed())
                socket.close();
        } catch (IOException ignored) {
        }
    }
}