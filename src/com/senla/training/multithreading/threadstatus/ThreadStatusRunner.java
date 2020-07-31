package com.senla.training.multithreading.threadstatus;

public class ThreadStatusRunner {

    public static void main(String[] args) {
        Thread thread = new Thread(new ThreadStatusDemonstrator());
        System.out.println("1: " + thread.getState());
        thread.start();
        System.out.println("2: " + thread.getState());
        sleep(100);
        synchronized (ThreadStatusDemonstrator.LOCK) {
            ThreadStatusDemonstrator.LOCK.notifyAll();
            System.out.println("3: " + thread.getState());
            ThreadStatusDemonstrator.sleep = true;
        }
        sleep(100);
        System.out.println("4: " + thread.getState());
        sleep(1000);
        System.out.println("5: " + thread.getState());
        ThreadStatusDemonstrator.active = false;
        synchronized (ThreadStatusDemonstrator.LOCK) {
            ThreadStatusDemonstrator.LOCK.notifyAll();
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
