package com.hanna;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import com.hanna.model.Grade;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Main {
    public static void main(String[] args) throws IOException {
        displayMenu();
        enterGrades();
        displayGrades();

    }

    public static void displayMenu() throws IOException {
        int choice;
        do {
            System.out.println("""
                    MENU
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
                    break;
            }
        } while (choice != 3);
    }

    public static int getIntInput(String message) {
        Scanner sc = new Scanner(System.in);
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
        Gson gson = new Gson();
        try (FileWriter fw = new FileWriter("data/grades.json", true)) {
            Grade grade = new Grade();
            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Subject: ");
            grade.setSubject(sc.nextLine());

            System.out.print("Enter Prelim Grade: ");
            grade.setPrelimGrade(sc.nextDouble());

            System.out.print("Enter Midterm Grade: ");
            grade.setMidtermGrade(sc.nextDouble());

            System.out.print("Enter Final Grade: ");
            grade.setFinalGrade(sc.nextDouble());

            String json = gson.toJson(grade);
            fw.write(json + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void displayGrades() throws IOException {
        FileReader fr = new FileReader("data/grades.json");

        Type gradeListType = new TypeToken<List<Grade>>() {
        }.getType();

        Gson gson = new Gson();
        List<Grade> grades = gson.fromJson(fr, gradeListType);

        for (Grade grade : grades) {
            System.out.println(grade);
        }
        fr.close();
    }

}