import java.io.*;
import java.util.*;

public class AttendanceApp {

    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();

        // sample students
        addStudent(students, "Hanna");
        addStudent(students, "Loren");

        if (recordAttendance(students, "Hanna", 1)) {
            System.out.println("Attendance recorded for Hanna.");
        }
        recordAttendance(students, "Hanna", 0);

        if (recordAttendance(students, "Loren", 1)) {
            System.out.println("Attendance recorded for Loren.");
        }

        // demonstrate error handling for non-existent student
        if (!recordAttendance(students, "Mark", 1)) {
            System.out.println("Error: Student 'Mark' not found.\n");
        }
        saveStudents(students, "attendance.txt");

        ArrayList<Student> loadedStudents = loadStudents("attendance.txt");

        // display loaded student
        System.out.println("\nLOADED STUDENTS:");
        displayAllStudents(loadedStudents);

        // demonstrate error handling for non-existent file
        System.out.println("\nLOADING TO NON-EXISTENT FILE:");
        loadStudents("nonexistent.txt");
    }

    public static void addStudent(ArrayList<Student> students, String name) {
        students.add(new Student(name));
    }

    public static boolean recordAttendance(ArrayList<Student> students, String studentName, int mark) {
        for (Student s : students) {
            if (s.name.equalsIgnoreCase(studentName)) {
                s.attendanceMarks.add(mark);
                return true;
            }
        }
        return false;
    }

    public static double getAttendancePercentage(Student student) {
        if (student.attendanceMarks.isEmpty())
            return 0;

        int present = 0;
        for (int mark : student.attendanceMarks) {
            if (mark == 1)
                present++;
        }

        return (present * 100.0) / student.attendanceMarks.size();
    }

    public static String getDisplayInfo(Student student) {
        return "Name: " + student.name +
                ", Attendance: " +
                String.format("%.2f", getAttendancePercentage(student)) + "%";
    }

    public static void displayAllStudents(ArrayList<Student> students) {
        for (Student s : students) {
            System.out.println(getDisplayInfo(s));
        }
    }

    // I/O persistence methods
    public static void saveStudents(ArrayList<Student> students, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            for (Student s : students) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.name);

                for (int mark : s.attendanceMarks) {
                    sb.append(",").append(mark);
                }

                writer.write(sb.toString());
                writer.newLine();
            }

            System.out.println("Students saved successfully to " + filename);

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public static ArrayList<Student> loadStudents(String filename) {
        ArrayList<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                Student s = new Student(parts[0]);

                for (int i = 1; i < parts.length; i++) {
                    try {
                        s.attendanceMarks.add(Integer.parseInt(parts[i]));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number format in file.");
                    }
                }

                students.add(s);
            }

            System.out.println("Students loaded successfully from " + filename);

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return students;
    }
}
