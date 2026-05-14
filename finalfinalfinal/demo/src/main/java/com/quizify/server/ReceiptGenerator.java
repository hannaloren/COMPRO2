package com.quizify.server;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class ReceiptGenerator {

    public static class Record {
        public String question;
        public String yourAnswer;
        public String correctAnswer;
        public boolean correct;

        public Record(String q, String y, String c, boolean r) {
            question = q;
            yourAnswer = y;
            correctAnswer = c;
            correct = r;
        }
    }

    public static void generate(String student, List<Record> records, int score, int total) {

        String file = "receipt_" + student + ".txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {

            pw.println("=== QUIZIFY RECEIPT ===");
            pw.println("Student: " + student);
            pw.println("Score: " + score + "/" + total);
            pw.println("-----------------------");

            for (Record r : records) {
                pw.println("Q: " + r.question);
                pw.println("Your Answer: " + r.yourAnswer);
                pw.println("Correct: " + r.correctAnswer);
                pw.println("Result: " + (r.correct ? "CORRECT" : "WRONG"));
                pw.println("-----------------------");
            }

            pw.println("FINAL SCORE: " + score + "/" + total);

            System.out.println("Receipt saved: " + file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}