package activity3;

import java.util.*;

public class Grades {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        // main menu loop
        do {
            System.out.println("""
                    MAIN MENU:
                    [1] Enter Grades
                    [2] Display Grades
                    [3] Exit
                    """);

            try { // try and catch block to handle non-integer input
                System.out.print("Enter Choice: ");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                choice = 0;
            }

            // subject choice loop
            switch (choice) {
                case 1:
                    int gradeChoice;
                    do {
                        System.out.println("""

                                Enter grade for:
                                [1] COMPRO
                                [2] DSA
                                [3] OOP
                                [4] Go Back
                                """);

                        try { // try and catch block to handle non-integer input
                            System.out.print("Enter Choice: ");
                            gradeChoice = sc.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input! Please enter a number.");
                            gradeChoice = 0;
                        }

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
                    break;

                case 2:
                    System.out.println();
                    System.out.println("\n====GRADE TABLE(to be implemented later)====");
                    break;

                case 3:
                    System.out.println();
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 3); // loop until user chooses to exit
    }
}
