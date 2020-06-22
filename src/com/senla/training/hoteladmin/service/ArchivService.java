package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

public interface ArchivService {
    void addClient(Client client);

    List<Client> getLastThreeResidents(Integer roomNumber);
}
