package com.quizify.model;

import java.io.FileWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Receipt implements Serializable {

    private ArrayList<String> logs = new ArrayList<>();

    public void add(String text) {
        logs.add(text);
    }

    public void exportToFile(String username, int score) {

        try {

            String fileName =
                    username +
                    "_receipt.txt";

            FileWriter fw =
                    new FileWriter(fileName);

            fw.write("====================================\n");
            fw.write("            QUIZIFY RECEIPT\n");
            fw.write("====================================\n");

            fw.write("Student: " + username + "\n");
            fw.write("Score: " + score + "\n");

            fw.write(
                    "Generated: "
                    + LocalDateTime.now()
                    + "\n"
            );

            fw.write("====================================\n\n");

            for (String log : logs) {

                fw.write(log + "\n");
                fw.write("------------------------------------\n");
            }

            fw.write("\n====================================\n");
            fw.write("           END OF RECEIPT\n");
            fw.write("====================================\n");

            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}