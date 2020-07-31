package com.senla.training.multithreading.threadstatus;

public class ThreadStatusRunner {

    public static void main(String[] args) {
        Thread thread = new Thread(new ThreadStatusDemonstrator());
        System.out.println("1: " + thread.getState());
        Object lock = ThreadStatusDemonstrator.getLOCK();
        thread.start();
        System.out.println("2: " + thread.getState());
        sleep(100);
        synchronized (lock) {
            lock.notifyAll();
            System.out.println("3: " + thread.getState());
            ThreadStatusDemonstrator.setSleep(true);
        }
        sleep(100);
        System.out.println("4: " + thread.getState());
        sleep(1000);
        System.out.println("5: " + thread.getState());
        ThreadStatusDemonstrator.setActive(false);
        synchronized (lock) {
            lock.notifyAll();
        }
        thread.interrupt();
        sleep(100);
        System.out.println("6: " + thread.getState());
    }

    public static void sleep(long n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted");
        }
    }
}
