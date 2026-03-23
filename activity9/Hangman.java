package activity9;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hangman {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        UserSign();

        final int maxPlayers = 50;
        final int numberOfGuesses = 5;

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

        showLeaderboard(playerNames, playerScores, currentPlayerCount);
    }

    public static void UserSign() {
        System.out.println("""
                USER DATA
                [1] Existing User
                [2] New User
                CHOICE:
                """);

        int user = sc.nextInt();
        sc.nextLine();

        switch (user) {
            case 1:
                loginUser();
                break;
            case 2:
                registerUser();
                break;
            default:
                System.out.println("Invalid option.");
                System.exit(0);
        }
    }

    public static void loginUser() {
        System.out.println("SIGN IN");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        String filename = "userdata.json";
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String content = "";

            while ((line = br.readLine()) != null) {
                content += line.trim();
            }

            content = content.replace("[", "").replace("]", "");

            if (!content.trim().isEmpty()) {
                String[] entries = content.split("\\},\\{");

                for (String entry : entries) {
                    entry = entry.replace("{", "").replace("}", "");

                    String[] fields = entry.split(",");

                    String fileUser = "";
                    String filePass = "";

                    for (String field : fields) {
                        String[] pair = field.split(":");

                        if (pair.length < 2)
                            continue;

                        String key = pair[0].replace("\"", "").trim();
                        String value = pair[1].replace("\"", "").trim();

                        if (key.equals("username"))
                            fileUser = value;
                        if (key.equals("password"))
                            filePass = value;
                    }

                    if (fileUser.equals(username) && filePass.equals(password)) {
                        found = true;
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("No user file found.");
        }

        if (found) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid credentials.");
            System.exit(0);
        }
    }

    public static void registerUser() {
        System.out.println("SIGN UP");
        System.out.print("Username: ");
        String newUser = sc.nextLine();
        System.out.print("Password: ");
        String newPass = sc.nextLine();

        String filename = "userdata.json";
        List<String> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(line);
            }
        } catch (IOException e) {

        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("[\n");

            for (int i = 0; i < users.size(); i++) {
                String line = users.get(i).trim();

                if (!line.equals("[") && !line.equals("]") && !line.isEmpty()) {
                    bw.write(line.replaceAll(",$", ""));
                    bw.write(",\n");
                }
            }

            bw.write("  { \"username\": \"" + newUser + "\", \"password\": \"" + newPass + "\" }\n");
            bw.write("]");

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("User registered successfully!");
    }

    public static void saveScore(String username, int score) {
        List<String> scores = new ArrayList<>();

        String existing = ReadFile("scores.json");

        if (existing != null && !existing.isEmpty()) {
            scores.addAll(Arrays.asList(existing.replace("[", "").replace("]", "").split("\n")));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("scores.json"))) {
            bw.write("[\n");

            for (String s : scores) {
                if (!s.trim().isEmpty() && !s.contains("]")) {
                    bw.write(s.replaceAll(",$", "") + "\n");
                }
            }

            bw.write("  { \"username\": \"" + username + "\", \"score\": " + score + " }\n");
            bw.write("]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int sumScores(String[] names, int[] scores, int count) {
        int total = 0;
        for (int i = 0; i < count; i++) {
            total += scores[i];
        }
        return total;
    }

    public static String ReadFile(String scoresjson) {
        List<String> lines = new ArrayList<>();
        String filename = "RandomStrings.txt";

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
        String word = ReadFile("scores.json");

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

            if (input.length() > 1) {
                if (input.equals(word)) {
                    points += 20;
                    guessed = word;
                    break;
                } else {
                    guessesLeft--;
                    continue;
                }
            }

            char letter = input.charAt(0);
            boolean correct = false;
            String newGuessed = "";

            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == letter) {
                    newGuessed += letter;
                    correct = true;
                } else {
                    newGuessed += guessed.charAt(i);
                }
            }

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

        if (!guessed.contains("*")) {
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