package week2;
<<<<<<< HEAD
=======

>>>>>>> 0ea658ae75b22082966368c9c7b363f16328ae69
public class Week2Act1 {
    public static void main(String[] args) {
        int [] theaterRow = new int [8];
        theaterRow[3] =  1;
<<<<<<< HEAD
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
=======

        System.out.println("Theater Row Seat Status:");
        for (int i = 0; i < theaterRow.length; i++) {
            System.out.println("Seat " + i + ": " + (theaterRow[i] == 0 ? "Available" : "Booked"));
        }

        int status = 0;
        for (int seat = 0; seat < theaterRow.length; seat++) {
            if (seat != 3){
>>>>>>> 0ea658ae75b22082966368c9c7b363f16328ae69
                status++;
            }   
        }
        System.out.println();
        System.out.println("Seat available: " + status);
    }
<<<<<<< HEAD

    public static void printRow(int[] row) {
        for (int seat : row) {
            System.out.print(seat == 0 ? "|-|" : "|x|");
        }
        System.out.println();
    }
}

=======
}
>>>>>>> 0ea658ae75b22082966368c9c7b363f16328ae69
