package com.quizify.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.quizify.model.Question;
import com.quizify.model.Receipt;
import com.quizify.model.Student;

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
        public boolean flagged = false;
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

                                if (s.getName()
                                                .equalsIgnoreCase(name)
                                                &&
                                                s.getCode()
                                                                .equals(code)) {

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

                        for (ClientHandler c : QuizServer.clients) {

                                if (c.username != null
                                                &&
                                                c.username.equalsIgnoreCase(name)
                                                &&
                                                c.state != State.FINISHED) {

                                        out.writeObject(
                                                        "ACCOUNT_ALREADY_LOGGED_IN");

                                        out.flush();

                                        socket.close();

                                        return;
                                }
                        }

                        out.writeObject("ACCESS_GRANTED");
                        out.flush();

                        QuizServer.clients.add(this);

                        // wait to start

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
                                System.out.println(
                                                "ROUND "
                                                                + round
                                                                + " / "
                                                                + QuizServer.questions.size());

                                out.writeObject(q);
                                out.flush();

                                out.writeObject(30);
                                out.flush();

                                long startTime = System.currentTimeMillis();

                                String answer = (String) in.readObject();

                                long endTime = System.currentTimeMillis();

                                long answerDuration = (endTime - startTime) / 1000;

                                totalAnswerTime += answerDuration;

                                if (answerDuration > 30) {

                                        answer = "NO ANSWER";
                                }

                                if (answer.equalsIgnoreCase(
                                                "NO ANSWER")) {

                                        timeoutCount++;
                                }

                                if (answerDuration <= 1
                                                &&
                                                !answer.equalsIgnoreCase(
                                                                "NO ANSWER")) {

                                        suspiciousCount++;

                                        System.out.println(
                                                        "[ANTI-CHEAT] "
                                                                        + username
                                                                        + " answered suspiciously fast!");
                                }

                                boolean correct = answer.equalsIgnoreCase(
                                                q.getCorrectAnswer());

                                if (correct) {
                                        score++;
                                }

                                if (suspiciousCount >= 3
                                                ||
                                                timeoutCount >= 5) {

                                        flagged = true;
                                }

                                receipt.add(
                                                "Question: "
                                                                + q.getPrompt()
                                                                + " | Your Answer: "
                                                                + answer
                                                                + " | Correct: "
                                                                + q.getCorrectAnswer()
                                                                + " | Result: "
                                                                + (correct
                                                                                ? "CORRECT"
                                                                                : "WRONG")
                                                                + " | Time: "
                                                                + answerDuration
                                                                + " sec");

                                Leaderboard.show(
                                                QuizServer.clients);

                                round++;
                        }

                        state = State.FINISHED;

                        receipt.add(
                                        "====================================");

                        receipt.add(
                                        "FINAL SCORE: "
                                                        + score);

                        receipt.add(
                                        "Average Answer Time: "
                                                        + (totalAnswerTime
                                                                        /
                                                                        QuizServer.questions.size())
                                                        + " sec");

                        receipt.add(
                                        "Suspicious Attempts: "
                                                        + suspiciousCount);

                        receipt.add(
                                        "Timeouts: "
                                                        + timeoutCount);

                        receipt.add(
                                        "Flagged: "
                                                        + (flagged
                                                                        ? "YES"
                                                                        : "NO"));

                        receipt.exportToFile(
                                        username,
                                        score);

                        out.writeObject(
                                        "GAME_OVER");

                        out.flush();

                        out.writeObject(
                                        "RECEIPT_SAVED");

                        out.flush();

                        QuizServer.checkIfAllFinished();

                } catch (Exception e) {

                        System.out.println(
                                        username
                                                        + " disconnected.");
                }
        }
}