package com.hanna;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hanna.model.Grade;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        displayMenu();

    }

    public static void displayMenu() throws IOException {
        int choice;
        do {
            System.out.println("""
                    \nMENU
                    [1] Enter Grades
                    [2] Display Grades
                    [3] Exit
                       """);

            choice = getIntInput("Enter Choice: ");

            switch (choice) {
                case 1:
                    enterGrades();
                    break;
                case 2:
                    displayGrades();
                    break;
                case 3:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 3);
    }

    public static int getIntInput(String message) {
        int value = 0;

        try {
            System.out.print(message);
            value = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number.");
            sc.nextLine();
        }
        return value;
    }

    public static void enterGrades() {
        Gson gson1 = new Gson();

        try {
            FileReader fr = new FileReader("data/grades.json");
            Type gradeListType = new TypeToken<List<Grade>>() {
            }.getType();
            List<Grade> grades = gson1.fromJson(fr, gradeListType);
            fr.close();

            if (grades == null) {
                grades = new java.util.ArrayList<>();
            }

            Grade grade = new Grade();
            sc.nextLine();

            System.out.print("Enter Subject: ");
            grade.setSubject(sc.nextLine());

            System.out.print("Enter Prelim Grade: ");
            grade.setPrelimGrade(sc.nextDouble());

            System.out.print("Enter Midterm Grade: ");
            grade.setMidtermGrade(sc.nextDouble());

            System.out.print("Enter Final Grade: ");
            grade.setFinalGrade(sc.nextDouble());

            grades.add(grade);
            Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fw = new FileWriter("data/grades.json");
            gson2.toJson(grades, fw);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void displayGrades() throws IOException {
        try (FileReader fr = new FileReader("data/grades.json")) {
            Type gradeListType = new TypeToken<List<Grade>>() {
            }.getType();

            Gson gson = new Gson();
            List<Grade> grades = gson.fromJson(fr, gradeListType);

            for (Grade grade : grades) {
                System.out.println(grade);
            }
        }
    }

}