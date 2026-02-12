package activity6;

import java.io.*;
import java.util.*;

import activity3.Grades;

public class GradeSearch {
    static List<Grade> grades;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        grades = new ArrayList<>();
         
        // MENU
        do {
            System.out.println("""

                    Menu
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
            
            // method call for choices
            switch (choice) {

                case 1:
                    addGrade(sc);
                    break;

                case 2:
                    displayGrades();
                    break;

                case 3:
                    String keyword = "";
                    System.out.print("Enter keyword: ");
                    keyword = sc.nextLine();
                    search(keyword);
                    break;

                case 4:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }

    // if switch case 1
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

            fw.write(subject + "," + prelim + "," + midterm + "," + finals + "\n"); // write the input into csv file
            System.out.println("Grade saved successfully!");

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Numbers only.");
            sc.nextLine();
        }
    }
    // switch case 2
    static void displayGrades() {

        File file = new File("grades.csv");

        if (!file.exists()) {
            System.out.println("No grades saved yet.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line; // temporary to store each line from csv file

            while ((line = br.readLine()) != null) {

                String[] data = line.split(","); // cuts or split strings when there is comma
                                                    // "COMPRO,99,99,99" -> "COMPRO" "99" "99" "99"

                Grade g = new Grade();
                g.subject = data[0];
                g.prelim = Double.parseDouble(data[1]);   // parse converts double to string
                g.midterm = Double.parseDouble(data[2]);
                g.finals = Double.parseDouble(data[3]);

                System.out.println(
                        g.subject + " | " + g.prelim +
                                " " + g.midterm +
                                "  " + g.finals);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static double tryParseDouble(String value){
        double parsed = -1;
        try{
            parsed = Double.parseDouble(value);
        }catch (NumberFormatException e){

        }
        return parsed;
    }
    // switch case 3
    public static void search(String s){
        System.out.println("Search results: ");;

        List<Grade> filtered = grades.stream().filter(grades ->
            grades.subject.toLowerCase().contains(s.toLowerCase()) 
            || grades.prelim == tryParseDouble(s)
            || grades.midterm == tryParseDouble(s)
            || grades.finals == tryParseDouble(s)
        ).toList();

        for (Grade g : filtered){
            System.out.printf("%-13s %-8s %.2f %4d\n", g.subject, g.prelim, g.midterm, g.finals);
        }

        if (filtered.size() == 0){
            System.out.println("No results found....");
        }
    }
    
    class Grade {
        String subject;
        double prelim;
        double midterm;
        double finals;
    }
}


