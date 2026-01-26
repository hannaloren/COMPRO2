package week3;

import java.util.Scanner;

public class Week3Act2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // problem 1
        System.out.println("-----PROBLEM 1-----\n");
        double[][] array = new double[3][4];
        System.out.println("Enter a 3-by-4 matrix row by row: ");

        for (int r = 0; r < array.length; r++) {
            for (int c = 0; c < array[r].length; c++) {
                array[r][c] = sc.nextDouble();
            }
        }

        System.out.println();
        for (int c = 0; c < array[0].length; c++) {
            double sum = sumColumn(array, c);
            System.out.println("Sum of the elements at column " + c + " is " + sum);
        }

        // problem 2
        System.out.println();
        System.out.println("-----PROBLEM 2-----\n");
        double[][] array2 = new double[4][4];
        System.out.println("Enter a 4-by-4 matrix row by row: ");

        for (int r2 = 0; r2 < array2.length; r2++) {
            for (int c = 0; c < array2[r2].length; c++) {
                array2[r2][c] = sc.nextDouble();
            }
        }
        double sum = sumMajorDiagonal(array2);
        System.out.println("Sum of the elements in the major diagonal is " + sum);

    }

    public static double sumColumn(double[][] m, int columnIndex) {
        double sum = 0;
        for (int r = 0; r < m.length; r++) {
            sum += m[r][columnIndex];
        }
        return sum;
    }

    public static double sumMajorDiagonal(double[][] m) {
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i][i];
        }
        return sum;
    }
}
