package com.quizify.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.quizify.model.Question;
import com.quizify.model.Receipt;

public class ClientHandler implements Runnable {

        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public String username;
        public int score = 0;

        public volatile State state = State.LOBBY;

        public int suspiciousCount = 0;
        public int timeoutCount = 0;
        public long totalAnswerTime = 0;

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

                        String name = (String) in.readObject();

                        String code = (String) in.readObject();

                        boolean valid = false;

                        String sharedCode = "QUIZ123";

                        for (String student : QuizServer.students) {

                                if (student.equalsIgnoreCase(name)
                                                &&
                                                code.equals(sharedCode)) {

                                        valid = true;

                                        username = student;

                                        break;
                                }
                        }

                        if (!valid) {

                                out.writeObject("ACCESS_DENIED");
                                out.flush();

                                socket.close();

                                return;
                        }

                        synchronized (QuizServer.activeUsers) {

                                if (QuizServer.activeUsers.containsKey(
                                                username.toLowerCase())) {

                                        out.writeObject(
                                                        "ACCOUNT_ALREADY_LOGGED_IN");

                                        out.flush();

                                        socket.close();

                                        return;
                                }

                                QuizServer.activeUsers.put(
                                                username.toLowerCase(),
                                                this);
                        }

                        QuizServer.clients.add(this);

                        out.writeObject("ACCESS_GRANTED");
                        out.flush();

                        while (!QuizServer.quizStarted) {

                                state = State.LOBBY;

                                Thread.sleep(500);
                        }

                        state = State.IN_QUIZ;

                        out.writeObject("START_QUIZ");
                        out.flush();

                        out.writeObject(
                                        QuizServer.questions.size());

                        out.flush();

                        for (Question q : QuizServer.questions) {

                                out.writeObject(q);
                                out.flush();

                                out.writeObject(30);
                                out.flush();

                                long startTime = System.currentTimeMillis();

                                String answer = (String) in.readObject();

                                long endTime = System.currentTimeMillis();

                                long duration = (endTime - startTime) / 1000;

                                totalAnswerTime += duration;

                                if (duration > 30) {

                                        answer = "NO ANSWER";
                                }

                                if (answer.equalsIgnoreCase(
                                                "NO ANSWER")) {

                                        timeoutCount++;
                                }

                                if (duration <= 1
                                                &&
                                                !answer.equalsIgnoreCase(
                                                                "NO ANSWER")) {

                                        suspiciousCount++;
                                }

                                boolean correct = answer.equalsIgnoreCase(
                                                q.getCorrectAnswer());

                                if (correct) {
                                        score++;
                                }

                                receipt.add(
                                                "Question: "
                                                                + q.getPrompt()

                                                                + "\nYour Answer: "
                                                                + answer

                                                                + "\nCorrect Answer: "
                                                                + q.getCorrectAnswer()

                                                                + "\n");
                        }

                        state = State.FINISHED;

                        receipt.add(
                                        "FINAL SCORE: "
                                                        + score
                                                        + " / "
                                                        + QuizServer.questions.size());

                        out.writeObject("QUIZ FINISHED");
                        out.flush();

                        out.writeObject(receipt);
                        out.flush();

                        out.writeObject(username);
                        out.flush();

                        out.writeObject(score);
                        out.flush();

                        QuizServer.checkIfAllFinished();

                } catch (Exception e) {

                        System.out.println(
                                        username + " disconnected.");
                }

                finally {

                        try {

                                if (username != null) {

                                        QuizServer.activeUsers.remove(
                                                        username.toLowerCase());
                                }

                                socket.close();

                        } catch (Exception ignored) {
                        }
                }
        }
}