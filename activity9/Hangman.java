package activity9;

import java.io.*;
import java.util.*;

public class Hangman {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        UserSign();
        // final variables
        final int maxPlayers = 50;
        final int numberOfGuesses = 5;

        // arrays to hold player names and scores
        String playerNames[] = new String[maxPlayers];
        int playerScores[] = new int[maxPlayers];
        int currentPlayerCount = 0;

        do {
            playerNames[currentPlayerCount] = getPlayerName();
            playerScores[currentPlayerCount] = playGame(numberOfGuesses);
            currentPlayerCount++;

            if (currentPlayerCount >= maxPlayers) {
                break;
            }
        } while (anotherPlayer());

        showLeaderboard(playerNames, playerScores, currentPlayerCount); // call leaderboard method
    }

    public static void UserSign() {

        System.out.println("""
                USER DATA
                [1] Existing User
                [2] New User
                CHOICE:
                """);

        int user = sc.nextInt();
        sc.nextLine(); // clear buffer

        switch (user) {
            case 1:
                System.out.println("SIGN IN");
                System.out.print("Username: ");
                String username = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();
                System.out.println("Login recorded.");
                break;
            case 2:

                try {
                    System.out.println("SIGN UP");
                    System.out.print("Username: ");
                    String newUser = sc.nextLine();
                    System.out.print("Set password: ");
                    String newPassword = sc.nextLine();
                    BufferedWriter bw = new BufferedWriter(
                            new FileWriter("userdata.json", true));
                    String json = "{ \"username\": \"" + newUser + "\", \"password\": \"" + newPassword + "\" }";
                    bw.write(json);
                    bw.newLine();
                    bw.close();
                    System.out.println("User saved to JSON.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    public static String ReadFile() {
        List<String> lines = new ArrayList<>();
        String filename = "compro2/activity9/RandomStrings.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            return null;
        }
        if (lines.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(lines.size());
        return lines.get(randomIndex);
    }

    public static String getPlayerName() {
        System.out.print("Player name: ");
        return sc.nextLine();
    }

    public static boolean anotherPlayer() {
        while (true) {
            System.out.print("\nAnother player? Enter y or n: ");
            String response = sc.nextLine().trim().toLowerCase();
            if (response.equals("y"))
                return true;
            if (response.equals("n"))
                return false;
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
        }
    }

    public static int playGame(int numberOfGuesses) {
        int index = (int) (Math.random() * 50);
        String word = ReadFile();

        if (word == null) {
            System.out.println("Error: Could not read word from file.");
            return 0;
        }

        String guessed = "";
        for (int i = 0; i < word.length(); i++)
            guessed += "*";

        int guessesLeft = numberOfGuesses;
        int points = 0;

        while (guessesLeft > 0 && guessed.contains("*")) {
            System.out.print("Enter a letter or guess the word " + guessed + " > ");
            String input = sc.nextLine().toLowerCase();

            if (input.length() > 1) { // guessing the whole word
                if (input.equals(word)) {
                    points += 20;
                    guessed = word;
                    break;
                } else {
                    guessesLeft--;
                    continue;
                }
            }

            char letter = input.charAt(0); // guessing a single letter
            boolean correct = false;
            String newGuessed = "";

            // reveal letters
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == letter) {
                    newGuessed += letter;
                    correct = true;
                } else {
                    newGuessed += guessed.charAt(i);
                }
            }

            // user entered the same letter or if the letter is not in the word
            if (guessed.indexOf(letter) != -1) {
                System.out.println(letter + " is already in the word");
            } else {
                guessed = newGuessed;
                if (correct) {
                    points += 1;
                } else {
                    guessesLeft--;
                    System.out.println(letter + " is not in the word");
                }
            }
        }

        if (guessed.indexOf("*") == -1) {
            System.out.println("\nCongratulations! You guessed the word: " + word);
        } else {
            System.out.println("\nGAME OVER");
            System.out.println("The word is " + word);
        }

        return points;
    }

    public static void showLeaderboard(String[] playerNames, int[] playerScores, int playerCount) {
        System.out.println("\n====Leaderboard====");
        for (int i = 0; i < playerCount; i++) {
            System.out.println(playerNames[i] + " " + playerScores[i] + " points");
        }
        sc.close();
    }

}
