import java.util.*;
import java.io.*;

public class TxtPractice {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(System.in);
             FileWriter fw = new FileWriter("PracticeOutput.txt")) {

            System.out.print("First Name: ");
            sb.append("First Name: ").append(sc.nextLine()).append("\n");

            System.out.print("Last Name: ");
            sb.append("Last Name: ").append(sc.nextLine()).append("\n");

            System.out.print("Age: ");
            sb.append("Age: ").append(sc.nextInt()).append("\n");
            sc.nextLine(); 

            System.out.print("Email: ");
            sb.append("Email: ").append(sc.nextLine()).append("\n");

            System.out.print("Input number: ");
            sb.append("Input number: ").append(sc.nextLine()).append("\n");

            fw.write(sb.toString());
            System.out.println("Data is saved...");

        } catch (InputMismatchException e) {
            System.out.println("Invalid input type.");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
