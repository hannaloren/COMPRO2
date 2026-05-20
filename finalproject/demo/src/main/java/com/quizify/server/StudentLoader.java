package com.quizify.server;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class StudentLoader {

    public static List<String> load(String path) {

        try {

            Gson gson = new Gson();

            String[] students =
                    gson.fromJson(
                            new FileReader(path),
                            String[].class
                    );

            return Arrays.asList(students);

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}