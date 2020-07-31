package com.senla.training.multithreading.communication;

public class CommunicationRunner {

    public static void main(String[] args) {
        Communicator communicator = new Communicator();
        Thread threadGenerator = new Thread(() ->
                communicator.generate());
        Thread threadGetter = new Thread(() ->
                communicator.get());

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
