package com.quizify.server;


import com.google.gson.*;
import common.model.*;

import java.io.FileReader;
import java.util.*;

public class QuestionLoader {

    public static List<Question> load(String path) throws Exception {

        Gson gson = new Gson();
        JsonArray arr = gson.fromJson(new FileReader(path), JsonArray.class);

        List<Question> list = new ArrayList<>();

        for (JsonElement el : arr) {
            JsonObject obj = el.getAsJsonObject();

            String type = obj.get("type").getAsString();

            if (type.equals("MCQ")) {
                list.add(new MultipleChoice(
                        obj.get("prompt").getAsString(),
                        gson.fromJson(obj.get("options"), String[].class),
                        obj.get("correctAnswer").getAsString()
                ));
            }

            if (type.equals("TF")) {
                list.add(new TrueFalse(
                        obj.get("prompt").getAsString(),
                        obj.get("correctAnswer").getAsString()
                ));
            }
        }

        return list;
    }
}