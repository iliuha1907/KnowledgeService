package com.senla.training.multithreading.systemtime;

public class SystemTimeRunner {

    public static void main(String[] args) {
        Thread demonstrator = new Thread(new SystemTimeDemonstrator(100));
        demonstrator.setDaemon(true);
        demonstrator.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted");
        }
    }
}

