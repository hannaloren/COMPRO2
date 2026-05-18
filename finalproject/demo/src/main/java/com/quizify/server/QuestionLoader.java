package com.quizify.server;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.quizify.model.Identification;
import com.quizify.model.MultipleChoice;
import com.quizify.model.Question;
import com.quizify.model.TrueFalse;

public class QuestionLoader {

    public static List<Question> load(String path) {

        List<Question> list = new ArrayList<>();
        path = "C:\\Users\\Asus Vivobook\\COMPRO2\\finalproject\\demo\\src\\main\\java\\com\\quizify\\resources\\questions.json";
        try {

            Gson gson = new Gson();

            JsonArray arr = gson.fromJson(
                    new FileReader(path),
                    JsonArray.class);

            for (JsonElement el : arr) {

                JsonObject obj = el.getAsJsonObject();

                String type = obj.get("type").getAsString();

                if (type.equals("MCQ")) {

                    list.add(
                            new MultipleChoice(
                                    obj.get("prompt").getAsString(),
                                    gson.fromJson(
                                            obj.get("options"),
                                            String[].class),
                                    obj.get("correctAnswer").getAsString()));
                }

                if (type.equals("TF")) {

                    list.add(
                            new TrueFalse(
                                    obj.get("prompt").getAsString(),
                                    obj.get("correctAnswer").getAsString()));
                }

                if (type.equals("ID")) {

                    list.add(
                            new Identification(
                                    obj.get("prompt").getAsString(),
                                    obj.get("correctAnswer").getAsString()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}