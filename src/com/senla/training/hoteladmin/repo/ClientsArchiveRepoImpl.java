package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientsArchiveRepoImpl implements ClientsArchiveRepo {

    private static ClientsArchiveRepoImpl instance;
    private List<Client> clients;

    private ClientsArchiveRepoImpl() {
        clients = new ArrayList<>();
    }

    public static ClientsArchiveRepo getInstance() {
        if (instance == null) {
            instance = new ClientsArchiveRepoImpl();
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

