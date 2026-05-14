package com.quizify.client;

package client;

import common.model.Question;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class QuizClient {

    public static void main(String[] args) {

        try (
                Socket socket = new Socket("localhost", 5000);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Scanner sc = new Scanner(System.in)
        ) {

            System.out.println(in.readObject());
            out.writeObject(sc.nextLine());

            System.out.println(in.readObject());
            out.writeObject(sc.nextLine());

            System.out.println(in.readObject());

            int total = in.readInt();

            for (int i = 0; i < total; i++) {

                Question q = (Question) in.readObject();

                q.display();

                System.out.print("Answer: ");
                out.writeObject(sc.nextLine());
            }

            System.out.println(in.readObject());

        } catch (Exception e) {
            System.out.println("Disconnected");
        }
    }
}