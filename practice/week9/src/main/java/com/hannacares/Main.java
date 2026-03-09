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

        Person person2 = new Person("Loren", "Obra", 20, "loren.obra@gmail.com", "09123456789", "Manila", true,
                "Filipino", "Male");

        Gson gson2 = new Gson();
        String json2 = gson2.toJson(person2);
        System.out.println(json2);

        FileWriter fw = new FileWriter("data/newperson.json");
        gson.toJson(person2, fw);
        fw.close();
    }

}