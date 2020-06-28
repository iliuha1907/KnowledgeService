package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientsArchiveRepositoryImpl implements ClientsArchiveRepository {

    private static ClientsArchiveRepositoryImpl instance;
    private List<Client> clients;

    private ClientsArchiveRepositoryImpl() {
        clients = new ArrayList<>();
    }

    public static ClientsArchiveRepository getInstance() {
        if (instance == null) {
            instance = new ClientsArchiveRepositoryImpl();
        }
        return instance;
    }
    @Override
    public List<Client> getClients() {
        return clients;
    }

    @Override
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}

