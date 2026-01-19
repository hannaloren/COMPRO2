package week2;

public class Week2Act1 {
    public static void main(String[] args) {
        int [] theaterRow = new int [8];
        theaterRow[3] =  1;

        System.out.println("Theater Row Seat Status:");
        for (int i = 0; i < theaterRow.length; i++) {
            System.out.println("Seat " + i + ": " + (theaterRow[i] == 0 ? "Available" : "Booked"));
        }

        int status = 0;
        for (int seat = 0; seat < theaterRow.length; seat++) {
            if (seat != 3){
                status++;
            }   
        }
        System.out.println();
        System.out.println("Seat available: " + status);
    }
}
