package com.senla.training.hoteladmin.model.archiv;

import com.senla.training.hoteladmin.model.client.Client;

public class Archivator {

    private ClientsArchive archive;
    private int currentClient = 0;

    public Archivator(ClientsArchive archive) {
        this.archive = archive;
    }

    public void addClient(Client client) {
        Client[] clients = archive.getClients();
        if (currentClient == clients.length) {
            throw new IllegalStateException("No more place in archive");
        }
        clients[currentClient++] = client;
        archive.setClients(clients);
    }

    public void displayLast3Residents(int roomNumber) {
        Client[] clients = archive.getClients();
        int times = 3;
        for (int i = currentClient - 1; i > -1; i--) {
            if (clients[i].getRoom().getNumber() == roomNumber) {
                System.out.println(clients[i]);
                times--;
                if (times == 0) {
                    break;
                }
            }
        }
    }
}

