package com.senla.training.multithreading.threadstatus;

public class ThreadStatusDemonstrator implements Runnable {
    private static final Object LOCK = new Object();
    private static boolean sleep = false;
    private static boolean active = true;

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

    public static Object getLOCK() {
        return LOCK;
    }

    public static void setActive(boolean active) {
        ThreadStatusDemonstrator.active = active;
    }

    public static void setSleep(boolean sleep) {
        ThreadStatusDemonstrator.sleep = sleep;
    }
}
