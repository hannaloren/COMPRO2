package com.quizify.server;

import com.google.gson.*;
import java.io.FileReader;
import java.util.*;

public class AuthService {

    private static final Set<String> students = new HashSet<>();

    static {
        try {
            Gson gson = new Gson();
            JsonArray arr = gson.fromJson(new FileReader("resources/students.json"), JsonArray.class);

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                String key = obj.get("name").getAsString() + "|" + obj.get("code").getAsString();
                students.add(key);
            }

        } catch (Exception e) {
            System.out.println("Error loading students.json");
        }
    }

    public static boolean authenticate(String name, String code) {
        return students.contains(name + "|" + code);
    }
}