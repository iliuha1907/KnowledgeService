package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

public interface ClientsRepository {
    void setClients(List<Client> clients);

    List<Client> getClients();
}

