import java.util.*;

public class Array1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (int i = 0; i < values.length; i++) {
            System.out.println(values[i]);
        }
        System.out.print("Enter a number: ");
        int number = sc.nextInt();
        int index = -1; // -1 means it is not found/true yet
        for (int j = 0; j < values.length; j++) {
            if (values[j] == number) {
                index = j; // found the number, set index to j
                break;
            }
        }

        if (index != -1) {
            System.out.println("Number found at index: " + index);
        } else {
            System.out.println("Number not found in the array.");
        }
    }
}