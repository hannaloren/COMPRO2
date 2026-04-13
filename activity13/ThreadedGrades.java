package activity13;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadedGrades {

    static Scanner sc = new Scanner(System.in);
    static List<String> data = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new FileReader("grades.json"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread menuThread = new Thread(() -> {
            while (true) {

                System.out.println("""
                        MENU
                        1. View Grades
                        2. Search Grades
                        3. Enter Grades
                        4. Edit Grades
                        5. Exit
                        """);

                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine(); // ✅ FIX #1 (prevent input bug)

                switch (choice) {

                    case 1 -> {
                        for (String line : data) {
                            line = line.trim();

                            if (line.equals("[") || line.equals("]") ||
                                    line.equals("{") || line.equals("}")) {
                                continue;
                            }

                            line = line.replace("\"", "")
                                    .replace(",", "")
                                    .replace(":", " = ");

                            System.out.println(line);
                        }
                    }

                    case 2 -> {
                        System.out.print("Enter subject to search: ");
                        String subject = sc.nextLine(); // FIXED (was next())

                        boolean found = false;

                        for (String line : data) {
                            line = line.trim();

                            if (line.equals("[") || line.equals("]") ||
                                    line.equals("{") || line.equals("}")) {
                                continue;
                            }

                            if (line.toLowerCase().contains(subject.toLowerCase())) {
                                String clean = line.replace("\"", "")
                                        .replace(",", "")
                                        .replace(":", " = ");

                                System.out.println(clean);
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("No matching subject found.");
                        }
                    }

                    case 3 -> {
                        System.out.print("Enter subject: ");
                        String subject = sc.nextLine();

                        System.out.print("Enter grade: ");
                        int grade = sc.nextInt();
                        sc.nextLine(); // FIX

                        String newGrade = "\"" + subject + "\": " + grade + ",";

                        data.add(newGrade);

                        saveToDisk();

                        System.out.println("Grade added.");
                    }

                    case 4 -> {
                        System.out.print("Enter subject to edit: ");
                        String subject = sc.nextLine();

                        System.out.print("Enter new grade: ");
                        int newGradeValue = sc.nextInt();
                        sc.nextLine(); // FIX

                        boolean found = false;

                        for (int i = 0; i < data.size(); i++) {
                            String line = data.get(i);

                            if (line.toLowerCase().contains("\"" + subject.toLowerCase() + "\"")) {

                                data.set(i, "\"" + subject + "\": " + newGradeValue + ",");

                                saveToDisk();

                                System.out.println("Grade updated.");
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("Subject not found.");
                        }
                    }

                    case 5 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }

                    default -> System.out.println("Invalid choice.");
                }
            }
        });

        Thread autoSaveThread = new Thread(() -> {
            while (true) {
                saveToDisk();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        menuThread.start();
        autoSaveThread.start();
    }

    private static void saveToDisk() {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("grades.json"))) {

            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}