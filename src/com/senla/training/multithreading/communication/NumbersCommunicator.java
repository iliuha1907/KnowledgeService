package com.senla.training.multithreading.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumbersCommunicator {
    private static final Integer TIME_TO_SLEEP = 100;
    private static final Integer AMOUNT = 5;
    private static final Object LOCK = new Object();
    private static List<Integer> numbers = new ArrayList<>();

    public void generate() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (LOCK) {
                System.out.println("Generating...");
                try {
                    Thread.sleep(TIME_TO_SLEEP);
                } catch (InterruptedException e) {
                    System.out.println("Sleep of generator was interrupted");
                    return;
                }

                if (numbers.size() >= AMOUNT) {
                    try {
                        System.out.println("No place!");
                        LOCK.wait();
                    } catch (Exception e) {
                        System.out.println("Wait of generator was interrupted");
                        return;
                    }
                } else {
                    numbers.add(new Random().nextInt());
                    LOCK.notifyAll();
                }
            }
        }
    }

    public void get() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (LOCK) {
                System.out.println("Getting...");
                try {
                    Thread.sleep(TIME_TO_SLEEP);
                } catch (InterruptedException e) {
                    System.out.println("Sleep of getter was interrupted");
                    return;
                }

                if (numbers.size() == 0) {
                    try {
                        System.out.println("Nothing to get!");
                        LOCK.wait();
                    } catch (Exception e) {
                        System.out.println("Wait of getter was interrupted");
                        return;
                    }
                } else {
                    System.out.println("Numbers:");
                    System.out.println(numbers);
                    numbers.clear();
                    LOCK.notifyAll();
                }
            }
        }
    }
}

