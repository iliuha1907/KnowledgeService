package com.senla.training.multithreading.systemtime;

public class SystemTimeDemonstrator implements Runnable {
    private Long timeToSleep;

    public SystemTimeDemonstrator(Long timeToSleep) {
        this.timeToSleep = timeToSleep;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("System time: " + System.currentTimeMillis());
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException ex) {
                System.out.println("Sleep of time demonstrator was interrupted");
                return;
            }
        }
    }
}

