package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

public interface ClientsArchiveRepo {
    List<Client> getClients();

    void setClients(List<Client> clients);
}

