package com.senla.training.multithreading.threadstatus;

public class ThreadStatusDemonstrator implements Runnable {
    public static final Object LOCK = new Object();
    public static boolean sleep = false;
    public static boolean active = true;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (LOCK) {
                    if (active) {
                        if (sleep) {
                            Thread.sleep(1000);
                        }
                        LOCK.wait();
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
                return;
            }
        }
    }
}
