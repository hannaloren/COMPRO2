package activity1;

import java.util.*;

public class TheaterSeating2D{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int[][] theaterSeat = new int[5][8];
        theaterSeat[2][5] = 1; // initially booked
        theaterSeat[0][0] = 1; // booked

        System.out.println("Theater Seating Chart (|-|=Available, |x|=Booked):");

        System.out.print("\nBook seat? Y/N: ");
        char userChoice = sc.next().charAt(0);

        if (userChoice == 'Y' || userChoice == 'y') {
            System.out.print("Enter row (1-5): ");
            int row = sc.nextInt() - 1;

            System.out.print("Enter seat (1-8): ");
            int seat = sc.nextInt() - 1;

            if (theaterSeat[row][seat] == 0) {
                theaterSeat[row][seat] = 1;
                System.out.println("\nSeat booked successfully!");
            } else {
                System.out.println("\nSeat already booked.");
            }

            // Updated display
            System.out.println("\nNew Theater Seating Chart (|-|=Available, |x|=Booked):");
            displayTheater(theaterSeat);
        }

    }

    public static void displayTheater(int[][] seat) {
        for (int i = 0; i < seat.length; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            printRow(seat[i]);
        }
        seatAvailable(seat);
    }

    public static void seatAvailable(int[][] seatingChart) {
        int available = 0;

        for (int i = 0; i < seatingChart.length; i++) {
            for (int j = 0; j < seatingChart[i].length; j++) {
                if (seatingChart[i][j] == 0) {
                    available++;
                }
            }
        }
        System.out.println("\nSeats available: " + available);
    }

    public static void printRow(int[] row) {
        for (int seat : row) {
            System.out.print(seat == 0 ? "|-|" : "|x|");
        }
        System.out.println();
    }
}