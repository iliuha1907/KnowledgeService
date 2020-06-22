package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientsRepoImpl implements ClientsRepo {
    private static ClientsRepoImpl instance;
    private List<Client> clients;

    private ClientsRepoImpl() {
        clients = new ArrayList<>();
    }

    public static ClientsRepo getInstance() {
        if (instance == null) {
            instance = new ClientsRepoImpl();
        }
        return instance;
    }

    @Override
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public List<Client> getClients() {
        return clients;
    }
}

