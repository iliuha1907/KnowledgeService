package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.LinkedList;
import java.util.List;

public class ClientsRepoImpl implements ClientsRepo {
    private static ClientsRepoImpl instance;
    private List<Client> clients;

    private ClientsRepoImpl() {
        clients = new LinkedList<>();
    }

    public static ClientsRepoImpl getInstance() {
        if (instance == null) {
            instance = new ClientsRepoImpl();
        }
        return instance;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }
}

