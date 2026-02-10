package activity5;

import java.io.*;
import java.util.*;

public class GradesArray {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("""
                    
                    Menu
                    [1] Add Grade for subject
                    [2] Display Grades
                    [3] Exit
                    """);

            try {
                System.out.print("Enter Choice: ");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Numbers only.");
                sc.nextLine();
                continue;
            }
            sc.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    addGrade(sc);
                    break;

                case 2:
                    displayGrades();
                    break;

                case 3:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 3);

        sc.close();
    }

    static void addGrade(Scanner sc) {
        try (FileWriter fw = new FileWriter("grades.csv", true)) {

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
            System.out.println("Grade saved successfully!");

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Numbers only.");
            sc.nextLine();
        }
    }

  
    static void displayGrades()  {
        File file = new File("grades.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                Grade g = new Grade();
                g.subject = data[0];
                g.prelim = Double.parseDouble(data[1]);
                g.midterm = Double.parseDouble(data[2]);
                g.finals = Double.parseDouble(data[3]);

                System.out.println(
                    g.subject + " | " + g.prelim +
                    "  " + g.midterm +
                    " " + g.finals
                );
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    
    static class Grade {
        String subject;
        double prelim;
        double midterm;
        double finals;
    }
}
