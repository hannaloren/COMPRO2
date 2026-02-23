package activity3;

import java.util.*;

public class Grades {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        // main menu loop
        do {
            System.out.println("""
                    MAIN MENU:
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
                    System.out.println("Invalid choice!");
            }

        } while (choice != 3);
    }

    // method for int input
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

    // method for entering grades
    public static void enterGrades() {
        int gradeChoice;

        do {
            System.out.println("""

                    Enter grade for:
                    [1] COMPRO
                    [2] DSA
                    [3] OOP
                    [4] Go Back
                    """);

            gradeChoice = getIntInput("Enter Choice: ");

            switch (gradeChoice) {
                case 1:
                    System.out.println("\nEnter grades for COMPRO");
                    System.out.println("Grades saved...");
                    break;

                case 2:
                    System.out.println("\nEnter grades for DSA");
                    System.out.println("Grades saved...");
                    break;

                case 3:
                    System.out.println("\nEnter grades for OOP");
                    System.out.println("Grades saved...");
                    break;

                case 4:
                    System.out.println("\nReturning to Main Menu...\n");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (gradeChoice != 4);
    }

    // Display Grades Method
    public static void displayGrades() {
        System.out.println("\n==== GRADE TABLE (to be implemented later) ====");
    }
}
