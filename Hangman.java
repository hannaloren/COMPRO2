import java.util.*;


public class ComproFinals {


    static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
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


        showLeaderboard(playerNames, playerScores, currentPlayerCount);  // call leaderboard method
    }


    public static String[] wordBank = {
            "java", "python", "hangman", "computer", "programming",
            "developer", "software", "keyboard", "internet", "variable",
            "function", "object", "class", "method", "array",
            "string", "boolean", "integer", "float", "double",
            "skeleton", "loop", "dominate", "paint", "garden",
            "opera", "leash", "butterfly", "buttocks", "cane",
            "organize", "blame", "decline", "competence", "glasses",
            "refer", "carry", "mature", "exile", "station",
            "slave", "elite", "request", "flood", "powder",
            "shrink", "ceiling", "forecast", "engine", "thesis"
    };


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
        int index = (int) (Math.random() * wordBank.length);
        String word = wordBank[index];


        String guessed = "";
        for (int i = 0; i < word.length(); i++)
            guessed += "*"; // hide the word


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


