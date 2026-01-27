package week3;
import java.util.Scanner; 

public class ExceptionPractice1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int num = sc.nextInt();

        try {
            int result = num / 0;
            System.out.println( num + " divided by 0 is " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: Division by zero is not allowed.");
        }
        System.out.println();
        inputNumber();
    }
    public static void inputNumber() {
        Scanner sc = new Scanner(System.in);
        int num = -1; //-1 means that is is initialized as a invalid number
        while (num < 0) { // Loop until a valid number is entered
            System.out.print("Enter a number: ");
            try {
                num = sc.nextInt();
                if (num < 0) {
                    System.out.println("You entered a negative number.");
                    System.out.println();
                } else {
                    System.out.println("You entered: " + num);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                System.out.println();
                sc.next(); // Clear the invalid input
            }
        }
    }
}