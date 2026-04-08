package com.hanna.Service;

import com.hanna.model.Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class FileHandler {

    public static List<Account> loadAccounts(String filename) {
        List<Account> accounts = new ArrayList<>();
        try (FileReader fr = new FileReader(filename)) {
            Gson gson = new Gson();
            Type accountsType = new TypeToken<List<Account>>() {
            }.getType();
            List<Account> acc = gson.fromJson(fr, accountsType);
            if (acc != null)
                accounts = acc;
        } catch (IOException e) {
            System.out.println("Error loading file: " + filename + " -> " + e.getMessage());
        }
        return accounts;
    }

    public static void saveAccounts(String filename, List<Account> accounts) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(filename)) {
            gson.toJson(accounts, fw);
            System.out.println("\nPlayer data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving file: " + filename + e.getMessage());
        }
    }
}
