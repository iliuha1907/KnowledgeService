package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

public interface ClientsRepo {
    void setClients(List<Client> clients);

    List<Client> getClients();
}

