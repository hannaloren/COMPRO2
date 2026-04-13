package activity13;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

class Grade {
    String subject;
    double grade;

    public Grade(String subject, double grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String toJson() {
        return "{\"subject\":\"" + subject + "\",\"grade\":" + grade + "}";
    }

    public static Grade fromJson(String json) {
        try {
            json = json.replace("{", "").replace("}", "");
            String[] parts = json.split(",");

            String subject = parts[0].split(":")[1].replace("\"", "").trim();
            double grade = Double.parseDouble(parts[1].split(":")[1].trim());

            return new Grade(subject, grade);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return subject + " : " + grade;
    }
}

public class ThreadedGrades {

    static final String FILE_NAME = "grades.json";
    static List<Grade> grades = new CopyOnWriteArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // ---------------- LOAD THREAD ----------------
    static class LoadThread extends Thread {
        public void run() {
            while (true) {
                try {
                    loadGradesIncremental();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println("Load error: " + e.getMessage());
                }
            }
        }
    }

    // ---------------- SAVE THREAD ----------------
    static class SaveThread extends Thread {
        public void run() {
            while (true) {
                try {
                    saveGrades();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println("Save error: " + e.getMessage());
                }
            }
        }
    }

    // ---------------- IMPROVED LOAD (NO RESET LOGIC) ----------------
    static void loadGradesIncremental() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            List<Grade> fileGrades = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                Grade g = Grade.fromJson(line);
                if (g != null) {
                    fileGrades.add(g);
                }
            }

            // UPDATE instead of reset:
            // - add new items
            // - update existing subjects
            for (Grade fileGrade : fileGrades) {

                boolean exists = false;

                for (Grade g : grades) {
                    if (g.subject.equalsIgnoreCase(fileGrade.subject)) {
                        g.grade = fileGrade.grade; // UPDATE ONLY
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    grades.add(fileGrade); // ADD new entry
                }
            }

            System.out.println("[Sync: grades updated from file]");

        } catch (FileNotFoundException e) {
            // file not created yet
        } catch (Exception e) {
            System.out.println("Load error: " + e.getMessage());
        }
    }

    // ---------------- SAVE ----------------
    static void saveGrades() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Grade g : grades) {
                writer.write(g.toJson());
                writer.newLine();
            }

            System.out.println("[Sync: saved grades]");

        } catch (Exception e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        new LoadThread().start();
        new SaveThread().start();

        while (true) {

            System.out.println("\n==== MENU ====");
            System.out.println("1. View Grades");
            System.out.println("2. Search Grades");
            System.out.println("3. Enter Grades");
            System.out.println("4. Edit Grades");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1 -> viewGrades();

                case 2 -> {
                    System.out.print("Enter subject: ");
                    String subject = sc.nextLine();

                    grades.stream()
                            .filter(g -> g.subject.equalsIgnoreCase(subject))
                            .forEach(System.out::println);
                }

                case 3 -> {
                    System.out.print("Enter subject: ");
                    String subject = sc.nextLine();

                    System.out.print("Enter grade: ");
                    double grade = sc.nextDouble();
                    sc.nextLine();

                    // prevent duplicates -> update instead
                    boolean updated = false;

                    for (Grade g : grades) {
                        if (g.subject.equalsIgnoreCase(subject)) {
                            g.grade = grade;
                            updated = true;
                            System.out.println("Updated existing subject!");
                            break;
                        }
                    }

                    if (!updated) {
                        grades.add(new Grade(subject, grade));
                        System.out.println("Added new grade!");
                    }
                }

                case 4 -> {
                    System.out.print("Enter subject to edit: ");
                    String subject = sc.nextLine();

                    boolean found = false;

                    for (Grade g : grades) {
                        if (g.subject.equalsIgnoreCase(subject)) {
                            System.out.print("Enter new grade: ");
                            g.grade = sc.nextDouble();
                            sc.nextLine();
                            found = true;
                            System.out.println("Updated!");
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

                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- VIEW ----------------
    static void viewGrades() {
        if (grades.isEmpty()) {
            System.out.println("No grades available.");
        } else {
            grades.forEach(System.out::println);
        }
    }
}