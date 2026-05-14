package com.quizify.server;

import com.google.gson.*;
import com.quizify.model.Student;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class StudentLoader {

    public static List<Student> load(String path) {

        List<Student> students = new ArrayList<>();
        path = "C:\\Users\\Asus Vivobook\\COMPRO2\\finalfinalfinal\\demo\\src\\main\\java\\com\\quizify\\resources\\students.json";
        try {

            Gson gson = new Gson();

            JsonArray arr = gson.fromJson(
                    new FileReader(path),
                    JsonArray.class);

            for (JsonElement el : arr) {

                JsonObject obj = el.getAsJsonObject();

                students.add(
                        new Student(
                                obj.get("name").getAsString(),
                                obj.get("code").getAsString()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
}