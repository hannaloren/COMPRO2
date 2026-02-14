package activity6;

import java.io.*;
import java.util.*;

public class GradeSearch {

    static List<Grade> grades = new ArrayList<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("""
                    \nMenu
                    [1] Add Grade for subject
                    [2] Display Grades
                    [3] Search
                    [4] Exit
                    """);

            try {
                System.out.print("Enter Choice: ");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Numbers only.");
                sc.nextLine();
                continue;
            }
            sc.nextLine();

            switch (choice) {

                case 1:
                    addGrade(sc);
                    break;
                case 2:
                    displayGrades();
                    break;
                case 3:
                    loadGradesFromFile(); // IMPORTANT FIX
                    System.out.print("Enter keyword: ");
                    String keyword = sc.nextLine();
                    search(keyword);
                    break;
                case 4:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }

        } while (choice != 4);

        sc.close();
    }

    // add grade method (switch case 1)
    static void addGrade(Scanner sc) {

        try (FileWriter fw = new FileWriter("grades.csv", true)) { // add input to

            System.out.print("Enter Subject: ");
            String subject = sc.nextLine();

            System.out.print("Enter Prelim Grade: ");
            double prelim = sc.nextDouble();

            System.out.print("Enter Midterm Grade: ");
            double midterm = sc.nextDouble();

            System.out.print("Enter Final Grade: ");
            double finals = sc.nextDouble();
            sc.nextLine();

            fw.write(subject + "," + prelim + "," + midterm + "," + finals + "\n");

            // fix into a list
            grades.add(new Grade(subject, prelim, midterm, finals));

            System.out.println("Grade saved successfully!");

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Numbers only.");
            sc.nextLine();
        }
    }

    // load grades from file into list
    static void loadGradesFromFile() {

        grades.clear();

        File file = new File("grades.csv");
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                grades.add(new Grade(
                        data[0],
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3])));
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // (swicth case 2) display grades method
    static void displayGrades() {

        loadGradesFromFile();

        if (grades.isEmpty()) {
            System.out.println("No grades saved yet.\n");
            return;
        }

        for (Grade g : grades) {
            System.out.printf("%-12s | %.2f %.2f %.2f\n",
                    g.subject, g.prelim, g.midterm, g.finals);
        }
    }

    // search method (switch case 3)
    static void search(String s) {

        System.out.println("\nSearch results:");

        double num = tryParseDouble(s);

        boolean found = false;

        for (Grade g : grades) {
            if (g.subject.toLowerCase().contains(s.toLowerCase())
                    || g.prelim == num
                    || g.midterm == num
                    || g.finals == num) {

                System.out.printf("%-12s | %.2f %.2f %.2f\n",
                        g.subject, g.prelim, g.midterm, g.finals);

                found = true;
            }
        }

        if (!found)
            System.out.println("No results found.");
    }

    static double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return -9999;
        }
    }

    // class
    static class Grade {
        String subject;
        double prelim;
        double midterm;
        double finals;

        Grade(String s, double p, double m, double f) {
            subject = s;
            prelim = p;
            midterm = m;
            finals = f;
        }
    }
}
