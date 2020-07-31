package com.senla.training.multithreading.threadname;

public class ThreadNameDemonstrator implements Runnable {
    private static final Object LOCK = new Object();

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (LOCK) {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Sleep of name demonstrator was interrupted");
                    return;
                }
            }
        }
    }
}
