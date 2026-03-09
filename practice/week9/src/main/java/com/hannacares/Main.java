package week9.src.main.java.com.hannacares;

import java.io.*;
import java.util.Scanner;

import com.google.gson.Gson;
import week9.src.main.java.com.hannacares.model.Person;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String json = "";

        Scanner sc = new Scanner(new File("data/person.json"));
        while (sc.hasNextLine()) {
            json += sc.nextLine();
        }
        Gson gson = new Gson();
        Person person = gson.fromJson(json, Person.class);

        System.out.println("HI!");
        System.out.println(person.toString());
    }
}