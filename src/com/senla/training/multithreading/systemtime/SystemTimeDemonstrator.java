package com.senla.training.multithreading.systemtime;

public class SystemTimeDemonstrator implements Runnable {
    private long timeToSleep;

    public SystemTimeDemonstrator(long timeToSleep) {
        this.timeToSleep = timeToSleep;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("System time: " + System.nanoTime());
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
