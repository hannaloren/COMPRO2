package activity1;

public class TheaterSeating {
    public static void main(String[] args) {
        int[] theaterRow = new int[8];
        theaterRow[3] = 1;

        System.out.println("Theater Row Seat Status (0 = Available, 1= Booked)");
        System.out.println();
        int[] row = theaterRow;
        for (int seat : row) {
            System.out.print(seat == 0 ? " |-| " : " |1| ");
        }
        System.out.println();
        int status = 0;
        for (int seat = 0; seat < theaterRow.length; seat++) {
            if (seat != theaterRow[seat]) {
                status++;
            }
        }
        System.out.println();
        System.out.println("Seat available: " + status);

    }

}
