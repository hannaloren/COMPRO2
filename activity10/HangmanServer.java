package activity10;

import java.io.*;
import java.net.*;
import java.util.*;

public class HangmanServer {
    public static void main(String[] args) {
        int port = 8000;
        int maxGuesses = 5;

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for client...");

            while (true) { // Keeps server running
                try (Socket client = server.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                    System.out.println("Client connected: " + client.getInetAddress());

                    // chechk if existing or new
                    String username = authenticateClient(in, out);
                    if (username == null)
                        continue;

                    // do the play game method/ clients terminal will show teh problem
                    out.println("\nWelcome " + username + "! Starting Hangman...");
                    int score = playGameWithClient(in, out, maxGuesses);

                    // save the score to scores.json
                    saveScore(username, score);
                    out.println("Game finished! Your score: " + score);
                    out.println("Goodbye!");
                    System.out.println("Client session ended");

                } catch (Exception e) {
                    System.out.println("Client session ended: " + e.getMessage());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String authenticateClient(BufferedReader in, PrintWriter out) throws IOException {
        out.println("[1] Existing User");
        out.println("[2] New User");
        out.println("Choice:");

        String choice = in.readLine();
        if (choice == null)
            return null;

        out.println("Username:");
        String user = in.readLine();
        out.println("Password:");
        String pass = in.readLine();

        if ("1".equals(choice)) {
            if (checkUser(user, pass)) {
                out.println("Login successful!");
                return user;
            } else {
                out.println("Invalid credentials.");
                return null;
            }
        } else {
            registerUser(user, pass);
            out.println("Registered successfully!");
            return user;
        }
    }

    public static int playGameWithClient(BufferedReader in, PrintWriter out, int numberOfGuesses) throws IOException {
        String word = getRandomWord();
        if (word == null) {
            out.println("Error loading word.");
            return 0;
        }

        word = word.toLowerCase();
        char[] displayArray = new char[word.length()]; // display the guessed and unguessed letters (using *)
        Arrays.fill(displayArray, '*');

        int guessesLeft = numberOfGuesses;
        int points = 0;

        while (guessesLeft > 0 && new String(displayArray).contains("*")) {
            out.println("Word: " + String.valueOf(displayArray));
            out.println("Lives left: " + guessesLeft);
            out.println("Guess the word:");

            String input = in.readLine();
            if (input == null || input.isEmpty())
                continue;
            input = input.toLowerCase();

            // whole word guess
            if (input.length() > 1) {
                if (input.equals(word)) {
                    displayArray = word.toCharArray(); // Reveal the whole word
                    points += 20;
                    break;
                } else {
                    guessesLeft--;
                    out.println("Wrong word!\n");
                }
            }
            // single letter guess
            else {
                char letter = input.charAt(0);
                boolean found = false;

                // Check if letter was already revealed
                if (new String(displayArray).indexOf(letter) != -1) {
                    out.println("You already guessed '" + letter + "'!");
                    continue;
                }

                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == letter) {
                        displayArray[i] = letter; // Update the display
                        found = true;
                    }
                }

                if (found) {
                    points++;
                    out.println("Correct letter!\n");
                } else {
                    guessesLeft--;
                    out.println("Wrong letter!\n");
                }
            }
        }

        // Final result message
        if (!new String(displayArray).contains("*")) {
            out.println("Congratulations! The word was: " + word);
        } else {
            out.println("GAME OVER! The word was: " + word);
        }

        return points;
    }

    // helper methods
    public static String getRandomWord() {
        File file = new File("RandomStrings.txt");
        if (!file.exists())
            return "apple"; // Fallback word

        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null)
                words.add(line.trim());
        } catch (IOException e) {
            return null;
        }

        return words.isEmpty() ? null : words.get(new Random().nextInt(words.size()));
    }

    public static boolean checkUser(String user, String pass) {
        File file = new File("userdata.json");
        if (!file.exists())
            return false;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("\"username\":\"" + user + "\"") && line.contains("\"password\":\"" + pass + "\""))
                    return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static void registerUser(String user, String pass) {
        File file = new File("userdata.json");
        List<String> data = new ArrayList<>();

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String content = br.readLine();
                if (content != null && content.length() > 2) {
                    content = content.substring(1, content.length() - 1); // remove [ ]
                    data.addAll(Arrays.asList(content.split("},\\{")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        data.add("{\"username\":\"" + user + "\",\"password\":\"" + pass + "\"}");

        try (PrintWriter pw = new PrintWriter(new FileWriter("userdata.json"))) {
            pw.println("[");
            for (int i = 0; i < data.size(); i++) {
                pw.print(data.get(i));
                if (i < data.size() - 1)
                    pw.println(",");
            }
            pw.println("]");
            pw.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveScore(String user, int score) {
    File file = new File("scores.json");
    List<String> scores = new ArrayList<>();

  
    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder fullContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                fullContent.append(line.trim());
            }
            String content = fullContent.toString();
            if (content.length() > 2) {
                // Strip the outer brackets [ ]
                content = content.substring(1, content.length() - 1);
                String[] entries = content.split("(?<=\\}),(?=\\{)");
                scores.addAll(Arrays.asList(entries));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // add new score record
    scores.add("{\"username\":\"" + user + "\",\"score\":" + score + ",\"date\":\"" + new Date() + "\"}");

    
    try (PrintWriter pw = new PrintWriter(new FileWriter("scores.json", false))) { 
        pw.println("[");
        for (int i = 0; i < scores.size(); i++) {
            pw.print(scores.get(i));
            if (i < scores.size() - 1) {
                pw.println(",");
            }
        }
        pw.println("\n]");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
