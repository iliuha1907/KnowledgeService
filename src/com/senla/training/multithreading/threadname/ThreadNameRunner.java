package com.senla.training.multithreading.threadname;

public class ThreadNameRunner {

    public static void main(String[] args) {
        Thread threadOne = new Thread(new ThreadNameDemonstrator());
        Thread threadTwo = new Thread(new ThreadNameDemonstrator());
        threadOne.start();
        threadTwo.start();
    }
}
