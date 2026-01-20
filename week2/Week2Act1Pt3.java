package week2;
import java.util.*;
public class Week2Act1Pt3 {
   
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int [][] theater = new int[5][8];
        theater[2][5] = 1; // 1 means it is booked
        theater[0][0] = 1;

        System.out.println("Theater Seating Chart (|-|=Available, |x|=Booked):");
        for (int i = 0; i < theater.length; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            printRow(theater[i]);
        }
        seatAvailable(theater[0]);

        System.out.print("Book seat? Y/N: ");
        char userChoice = sc.next().charAt(0);
        if (userChoice == 'Y' || userChoice == 'y') {
            System.out.print("Enter row (1-5): ");
            int row = sc.nextInt() - 1;
            System.out.print("Enter seat (1-8): ");
            int seat = sc.nextInt() - 1;
            if (theater[row][seat] == 0) {
                theater[row][seat] = 1; // Book the seat
                System.out.println("Seat booked successfully!");
            } else {
                System.out.println("Seat already booked.");
            }

        System.out.println("New Theater Seating Chart (|-|=Available, |x|=Booked):");
        for (int i = 0; i < theater.length; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            printRow(theater[i]);
        }
        seatAvailable(theater[0]);
        }

    }

    public static void seatAvailable(int[] theaterRow) {
        int status = 0;
        for (int seat = 0; seat < theaterRow.length; seat++) {
            if (seat != theaterRow[seat]) {
                status++;
            }   
        }
        System.out.println();
        System.out.println("Seat available: " + status);
    }
    public static void printRow(int[] row) {
        for (int seat : row) {
            System.out.print(seat == 0 ? "|-|" : "|x|");
        }
        System.out.println();
    }
}



