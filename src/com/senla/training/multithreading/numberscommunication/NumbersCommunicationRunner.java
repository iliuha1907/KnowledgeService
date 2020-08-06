package com.senla.training.multithreading.numberscommunication;

public class NumbersCommunicationRunner {

    public static void main(String[] args) {
        NumbersCommunicator numbersCommunicator = new NumbersCommunicator();
        Thread threadGenerator = new Thread(() ->
                numbersCommunicator.generate());
        Thread threadGetter = new Thread(() ->
                numbersCommunicator.get());

        threadGenerator.start();
        threadGetter.start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted");
        }
        threadGenerator.interrupt();
        threadGetter.interrupt();
    }
}

