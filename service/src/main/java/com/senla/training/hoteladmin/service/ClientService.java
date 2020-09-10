package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

public interface ClientService {

    void addClient(String firstName, String lastName);

    List<Client> getClients();

    Long getNumberOfClients();

    void exportClients();

    void importClients();
}
