package com.senla.training.hotelAdmin.service;

import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.util.sort.ClientsSortCriterion;

import java.util.Date;
import java.util.List;

public interface ClientService {
    void addResident(String firstName, String lastName, Date arrival, Date departure);

    void removeResident(Client resident);

    void removeResidentById(Integer id);

    List<Client> getSortedClients(ClientsSortCriterion criterion);

    Integer getNumberOfResidents();

    Client getClientById(Integer id);

    List<Client> getLastThreeResidents(Integer roomId);

    void exportClients();

    void importClients();

    void updateClient(Client client);
}

