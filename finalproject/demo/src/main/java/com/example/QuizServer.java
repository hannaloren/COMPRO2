package com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class QuizServer {
    private static final int PORT = 8080;
    private static List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static List<Question> questions = new ArrayList<>();
    private static List<Student> students = new ArrayList<>();
    private static Map<String, ClientHandler> activeClients = new ConcurrentHashMap<>();
    private static Map<String, Long> lastSeen = new ConcurrentHashMap<>();
    private static List<String> cheaters = Collections.synchronizedList(new ArrayList<>());
    private static volatile boolean gameStarted = false;
    private static volatile int questionIndex = 0;
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        loadStudents();
        loadQuestions();
        startTerminalDashboard();
        startInactivityChecker();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("SERVER STARTED ON PORT " + PORT);

            Thread acceptThread = new Thread(() -> {
                while (!gameStarted) {
                    try {
                        Socket socket = serverSocket.accept();
                        ClientHandler client = new ClientHandler(socket);
                        clients.add(client);
                        client.start();
                    } catch (Exception e) {
                        if (!gameStarted)
                            System.out.println("Accept error: " + e.getMessage());
                    }
                }
            });
            acceptThread.start();

            Scanner sc = new Scanner(System.in);
            System.out.println("Type START to begin quiz...");
            while (sc.hasNextLine()) {
                if (sc.nextLine().equalsIgnoreCase("START")) {
                    gameStarted = true;
                    break;
                }
            }
            runGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean verifyStudent(String name, String pass, String code, ClientHandler handler) {
        String key = code.toLowerCase();
        if (activeClients.containsKey(key))
            return false;

        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name) && s.getPassword().equals(pass)
                    && s.getCode().equalsIgnoreCase(code)) {
                activeClients.put(key, handler);
                lastSeen.put(key, System.currentTimeMillis());
                return true;
            }
        }
        return false;
    }

    public static void updateHeartbeat(String code) {
        lastSeen.put(code.toLowerCase(), System.currentTimeMillis());
    }

    public static void removeUser(String code) {
        activeClients.remove(code.toLowerCase());
        lastSeen.remove(code.toLowerCase());
    }

    public static void reportCheater(String name, String reason) {
        cheaters.add(name + " | " + reason);
    }

    private static void runGame() {
        broadcast("=== QUIZ STARTED ===");
        for (Question q : questions) {
            questionIndex++;
            for (ClientHandler c : clients)
                c.enableAnswering();

            broadcast(gson.toJson(q));
            broadcast("ANSWER_NOW");

            for (int t = 30; t >= 0; t--) {
                broadcast("TIMER:" + t);
                sleep(1000);
            }

            for (ClientHandler c : clients)
                c.disableAnswering();
            broadcast("STOP_ANSWER");

            for (ClientHandler c : clients) {
                String ans = c.getLatestAnswer();
                if (ans != null && ans.equalsIgnoreCase(q.getAnswer())) {
                    c.addScore();
                    c.sendMessage("CORRECT");
                } else {
                    c.sendMessage("WRONG! Correct was: " + q.getAnswer());
                }
            }
            broadcastLeaderboard();
            sleep(3000);
        }
        broadcast("GAME_OVER");
    }

    private static void broadcastLeaderboard() {
        List<ClientHandler> sorted = new ArrayList<>(clients);
        sorted.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        StringBuilder board = new StringBuilder("\n--- LEADERBOARD ---\n");
        int rank = 1;
        for (ClientHandler c : sorted) {
            board.append(rank++).append(". ").append(c.getPlayerName()).append(" | Score: ").append(c.getScore())
                    .append("\n");
        }
        broadcast(board.toString());
    }

    private static void broadcast(String msg) {
        for (ClientHandler c : clients)
            c.sendMessage(msg);
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    private static void startTerminalDashboard() {
        Thread t = new Thread(() -> {
            while (true) {
                sleep(2000);
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("=========== QUIZ DASHBOARD ===========");
                System.out.println("ACTIVE USERS: " + activeClients.size());
                System.out.println("CHEATERS: " + cheaters.size());
                System.out.println("=====================================");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private static void startInactivityChecker() {
        Thread t = new Thread(() -> {
            while (true) {
                sleep(5000);
                long now = System.currentTimeMillis();
                lastSeen.entrySet().removeIf(entry -> (now - entry.getValue() > 60000));
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private static void loadStudents() {
        try (Reader r = new FileReader("data/students.json")) {
            Type t = new TypeToken<ArrayList<Student>>() {
            }.getType();
            students = new Gson().fromJson(r, t);
        } catch (Exception e) {
            System.out.println("Student load error");
        }
    }

    private static void loadQuestions() {
        try (Reader r = new FileReader("data/questions.json")) {
            Type t = new TypeToken<ArrayList<Question>>() {
            }.getType();
            questions = new Gson().fromJson(r, t);
        } catch (Exception e) {
            System.out.println("Question load error");
        }
    }
}