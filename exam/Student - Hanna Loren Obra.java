import java.util.ArrayList;

public class Student {
    String name;
    ArrayList<Integer> attendanceMarks;

    public Student(String name) {
        this.name = name;
        this.attendanceMarks = new ArrayList<>();
    }
}
