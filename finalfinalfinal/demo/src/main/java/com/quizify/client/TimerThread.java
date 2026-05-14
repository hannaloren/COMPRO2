package com.quizify.client;

public class TimerThread extends Thread {

    private int seconds;

    public volatile boolean running = true;

    public TimerThread(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public void run() {

        try {

            for (int i = seconds; i >= 0 && running; i--) {

                System.out.printf("\rTime Left: %-2d seconds ", i);

                Thread.sleep(1000);
            }

            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}