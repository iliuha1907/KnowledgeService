package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

public interface ClientsArchiveRepository {
    List<Client> getClients();

    void setClients(List<Client> clients);
}

