package actrivity1;

public class TheaterSeating {
    public static void main(String[] args) {
        int[] theaterRow = new int[8];
        theaterRow[3] = 1;
        theaterRow[5] = 1;
        theaterRow[2] = 1;
        theaterRow[7] = 1;
        theaterRow[4] = 1;

        System.out.println("Theater Row Seat Status:");
        printRow(theaterRow);
        printRow(theaterRow);
        printRow(theaterRow);
        printRow(theaterRow);
        printRow(theaterRow);

        seatAvailable(theaterRow);
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
