package com.quizify.server;

import common.model.Question;
import server.ReceiptGenerator.Record;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {

            out.writeObject("NAME:");
            String name = (String) in.readObject();

            out.writeObject("CODE:");
            String code = (String) in.readObject();

            if (!AuthService.authenticate(name, code)) {
                out.writeObject("ACCESS DENIED");
                return;
            }

            out.writeObject("WELCOME " + name);

            int score = 0;
            List<Record> records = new ArrayList<>();

            out.writeInt(QuizServer.questions.size());
            out.flush();

            for (Question q : QuizServer.questions) {

                out.writeObject(q);

                String ans = (String) in.readObject();

                boolean correct = q.checkAnswer(ans);

                if (correct) score++;

                records.add(new Record(
                        q.getPrompt(),
                        ans,
                        q.getCorrectAnswer(),
                        correct
                ));
            }

            out.writeObject("FINISHED SCORE: " + score);

            ReceiptGenerator.generate(name, records, score, QuizServer.questions.size());

        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }
}