package com.quizify.server;



import com.quizify.model;

import java.net.*;
import java.util.*;

public class QuizServer {

    public static List<Question> questions;

    public static void main(String[] args) throws Exception {

        questions = QuestionLoader.load("resources/questions.json");

        ServerSocket server = new ServerSocket(5000);

        System.out.println("QUIZIFY SERVER STARTED");

        while (true) {
            Socket socket = server.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }
}