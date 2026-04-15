
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadedGrade {

    static List<Grade> grades = new CopyOnWriteArrayList<>();
    private static final String FILE_NAME = "grades.json";

    public static void main(String[] args) {
        loadGrades();

      
        Thread saverThread = new Thread(() -> {
            while (true) {
                try {
                    // Wait for 5 seconds
                    Thread.sleep(5000);
                    saveGradesToJson();
                } catch (InterruptedException e) {
                    break; // Exit thread if interrupted
                }
            }
        });
        
       
        saverThread.setDaemon(true); 
        saverThread.start();

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("""
                    \nMenu (Background Auto-save every 5s)
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
                case 1 -> addGrade(sc);
                case 2 -> displayGrades();
                case 3 -> {
                    System.out.print("Enter search keyword: ");
                    String keyword = sc.nextLine();
                    search(keyword);
                }
                case 4 -> {
                    System.out.println("Finalizing save and exiting...");
                    saveGradesToJson(); 
                }
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 4);

        sc.close();
    }

    static void addGrade(Scanner sc) {
        try {
            System.out.print("Enter Subject: ");
            String subject = sc.nextLine();
            System.out.print("Enter Prelim Grade: ");
            double prelim = sc.nextDouble();
            System.out.print("Enter Midterm Grade: ");
            double midterm = sc.nextDouble();
            System.out.print("Enter Final Grade: ");
            double finals = sc.nextDouble();
            sc.nextLine();

            grades.add(new Grade(subject, prelim, midterm, finals));
            System.out.println("Grade added. Background thread will sync to JSON soon.");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Use numbers for grades.");
            sc.nextLine();
        }
    }

    // --- JSON PERSISTENCE LOGIC ---
    static void saveGradesToJson() {
        if (grades.isEmpty()) return;

        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            StringBuilder json = new StringBuilder("[\n");
            for (int i = 0; i < grades.size(); i++) {
                Grade g = grades.get(i);
                json.append("  {\n")
                    .append("    \"subject\": \"").append(g.subject).append("\",\n")
                    .append("    \"prelim\": ").append(g.prelim).append(",\n")
                    .append("    \"midterm\": ").append(g.midterm).append(",\n")
                    .append("    \"finals\": ").append(g.finals).append("\n")
                    .append("  }");
                if (i < grades.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("]");
            fw.write(json.toString());
           
        } catch (IOException e) {
            System.err.println("Error during background save: " + e.getMessage());
        }
    }

    static void loadGrades() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) content.append(line);

            String raw = content.toString().trim();
            if (raw.length() <= 2) return; // Empty array []

           
            String clean = raw.substring(1, raw.length() - 1); // remove outer []
            String[] objects = clean.split("\\},");

            for (String obj : objects) {
                String entry = obj.replace("{", "").replace("}", "").replace("\"", "").trim();
                String[] fields = entry.split(",");
                
                String subject = "";
                double p = 0, m = 0, f = 0;

                for (String field : fields) {
                    String[] kv = field.split(":");
                    String key = kv[0].trim();
                    String val = kv[1].trim();

                    switch (key) {
                        case "subject" -> subject = val;
                        case "prelim" -> p = Double.parseDouble(val);
                        case "midterm" -> m = Double.parseDouble(val);
                        case "finals" -> f = Double.parseDouble(val);
                    }
                }
                grades.add(new Grade(subject, p, m, f));
            }
        } catch (Exception e) {
            System.out.println("No existing JSON data loaded.");
        }
    }

    static void displayGrades() {
        if (grades.isEmpty()) {
            System.out.println("List is empty.");
            return;
        }
        System.out.printf("%-15s | %-7s | %-7s | %-7s\n", "Subject", "Prelim", "Midterm", "Final");
        for (Grade g : grades) {
            System.out.printf("%-15s | %-7.2f | %-7.2f | %-7.2f\n", g.subject, g.prelim, g.midterm, g.finals);
        }
    }

    static void search(String s) {
        double num = tryParseDouble(s);
        boolean found = false;
        for (Grade g : grades) {
            if (g.subject.toLowerCase().contains(s.toLowerCase()) || g.prelim == num || g.midterm == num || g.finals == num) {
                System.out.printf("%-12s | %.2f %.2f %.2f\n", g.subject, g.prelim, g.midterm, g.finals);
                found = true;
            }
        }
        if (!found) System.out.println("No matching records.");
    }

    static double tryParseDouble(String value) {
        try { return Double.parseDouble(value); } 
        catch (NumberFormatException e) { return -9999; }
    }

    static class Grade {
        String subject;
        double prelim, midterm, finals;
        Grade(String s, double p, double m, double f) {
            this.subject = s; this.prelim = p; this.midterm = m; this.finals = f;
        }
    }
}