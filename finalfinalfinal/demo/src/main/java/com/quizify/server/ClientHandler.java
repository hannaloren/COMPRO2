package com.quizify.server;

import com.quizify.model.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {

    private Socket socket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public String username;
    public int score = 0;

    public volatile State state = State.LOBBY;

    private Receipt receipt = new Receipt();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            out = new ObjectOutputStream(
                    socket.getOutputStream());

            out.flush();

            in = new ObjectInputStream(
                    socket.getInputStream());

            out.writeObject("ENTER_NAME");
            out.flush();

            String name = (String) in.readObject();

            out.writeObject("ENTER_CODE");
            out.flush();

            String code = (String) in.readObject();

            boolean valid = false;

            for (Student s : QuizServer.students) {

                if (s.getName().equalsIgnoreCase(name)
                        &&
                        s.getCode().equals(code)) {

                    valid = true;
                    username = s.getName();
                    break;
                }
            }

            if (!valid) {

                out.writeObject("ACCESS_DENIED");
                out.flush();

                socket.close();
                return;
            }

            out.writeObject("ACCESS_GRANTED");
            out.flush();

            QuizServer.clients.add(this);

            while (!QuizServer.gameStarted) {

                state = State.LOBBY;
                Thread.sleep(1000);
            }

            state = State.IN_GAME;
            out.writeObject("START_GAME");
            out.flush();

            out.writeObject(
                    QuizServer.questions.size());

            out.flush();

            int round = 1;

            for (Question q : QuizServer.questions) {

                System.out.println();
                System.out.println("ROUND " + round);

                out.writeObject(q);
                out.flush();

                out.writeObject(30);
                out.flush();

                ExecutorService executor = Executors.newSingleThreadExecutor();

                Future<String> future = executor.submit(() -> (String) in.readObject());

                String answer;

                try {

                    answer = future.get(
                            30,
                            TimeUnit.SECONDS);

                } catch (TimeoutException e) {

                    answer = "NO ANSWER";

                    future.cancel(true);
                }

                executor.shutdownNow();

                boolean correct = answer.equalsIgnoreCase(
                        q.getCorrectAnswer());

                if (correct) {
                    score++;
                }

                receipt.add(
                        "Question: " + q.getPrompt()
                                + " | Your Answer: "
                                + answer
                                + " | Correct Answer: "
                                + q.getCorrectAnswer()
                                + " | Result: "
                                + (correct ? "CORRECT" : "WRONG"));

                Leaderboard.show(QuizServer.clients);

                round++;
            }

            state = State.FINISHED;

            QuizServer.checkIfAllFinished();
            out.writeObject("GAME_OVER");
            out.flush();

            receipt.exportToFile(username, score);

            out.writeObject(
                    "RECEIPT_SAVED");

            out.flush();

        } catch (Exception e) {

            System.out.println(
                    username + " disconnected.");
        }
    }
}