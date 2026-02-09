package activity5;

import java.io.*;
import java.util.*;

import javax.security.auth.Subject;

public class GradesArray {
    public static void main(String[] args) throws Exception {
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<Double> grades = new ArrayList<>(); 
        Scanner sc = new Scanner(System.in);
        int choice;
        int count = 0;
        do{
        System.out.println("""
                Menu
                [1] Add Grade for subject
                [2] Display Grades
                [3] Exit
                """);

                try{
                    System.out.print("Enter Choice: ");
                    choice = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a number.");
                    choice = 0; // Reset choice to avoid unintended behavior
                }
                sc.nextLine(); // Consume newline

                switch (choice) {
                case 1: 
                    if (count >= 50) {
                        System.out.println("Maximum entries reached.");
                        break;
                    }

                    System.out.print("Enter Subject: ");
                    subjects.add(sc.nextLine());

                    try {
                        System.out.print("Enter Prelim Grade: ");
                        double prelim = sc.nextDouble();

                        System.out.print("Enter Midterm Grade: ");
                        double midterm = sc.nextDouble();

                        System.out.print("Enter Final Grade: ");
                        double finals = sc.nextDouble();
                        
                        grades.add(prelim + midterm + finals);

                        count++;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Numbers only.");
                        sc.nextLine();
                    
                        } 
                case 2: 
                    try{
                        BufferedReader br = new BufferedReader(new FileReader("C:/Users/STUDENTS/compro2/activity5/grades.csv"));
                        String line;
                        br.readLine(); // Skip header
                        while ((line = br.readLine()) != null) {
                            String[] data = line.split(",");
                            Grade g = new Grade();
                            g.subject = data[0];
                            g.prelim = sc.nextDouble();
                            g.midterm = sc.nextDouble();
                            g.finals = sc.nextDouble();
                            System.out.println("Subject: " + g.subject);   
                    }
                    br.close();
                } catch (IOException e) {
                        System.out.println("Error reading file: " + e.getMessage());
                }
                break;
        
        }
    } while (choice != 3);
       
    } 
     static class Grade{
            String subject;
            double prelim;
            double midterm;
            double finals;
        }
    

}
