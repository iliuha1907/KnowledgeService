package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsRepositoryImpl implements ClientsRepository {
    private static ClientsRepositoryImpl instance;
    private List<Client> clients;

    private ClientsRepositoryImpl() {
        clients = new ArrayList<>();
    }

    public static ClientsRepository getInstance() {
        if (instance == null) {
            instance = new ClientsRepositoryImpl();
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

