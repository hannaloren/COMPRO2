package activity4;

import java.io.*;
import java.util.*;

public class GradesArray {

    static String[] subject = new String[50];
    static double[][] grades = new double[50][3]; // Prelim, Midterm, Finals
    static int count = 0;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("""

                    Menu
                    [1] Add Grade for subject
                    [2] Exit
                    """);

            try {
                System.out.print("Enter Choice: ");
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                choice = 0;
            }
            sc.nextLine();

            switch (choice) {
                case 1: {
                    if (count >= 50) {
                        System.out.println("Maximum entries reached.");
                        break;
                    }

                    System.out.print("Enter Subject: ");
                    subject[count] = sc.nextLine();

                    try {
                        System.out.print("Enter Prelim Grade: ");
                        grades[count][0] = sc.nextDouble();

                        System.out.print("Enter Midterm Grade: ");
                        grades[count][1] = sc.nextDouble();

                        System.out.print("Enter Final Grade: ");
                        grades[count][2] = sc.nextDouble();

                        count++;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Numbers only.");
                        sc.nextLine();
                    }
                }
            }

        } while (choice != 2);

        writeData();
        sc.close();
    }

    public static void writeData() {
        // method to write data to CSV file
        StringBuilder sb = new StringBuilder();
        sb.append("Subject,Prelim,Midterm,Final\n");

        for (int r = 0; r < count; r++) {
            sb.append(subject[r]);
            for (int c = 0; c < 3; c++) {
                sb.append(",").append(grades[r][c]);
            }
            sb.append("\n");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv"))) { // bufferedWriter to write to CSV
                                                                                   // file
            bw.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\nSaved Data:");
        System.out.println(sb);
    }
}
