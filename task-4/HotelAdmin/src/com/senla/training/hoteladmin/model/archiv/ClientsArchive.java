package com.senla.training.hoteladmin.model.archiv;

import com.senla.training.hoteladmin.model.client.Client;

public class ClientsArchive {

    private Client[] clients;

    public ClientsArchive(int size){
        clients = new Client[size];
    }

    public Client[] getClients() {
        return clients;
    }

    public void setClients(Client[] clients) {
        this.clients = clients;
    }
}
