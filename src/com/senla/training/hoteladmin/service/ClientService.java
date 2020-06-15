package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.client.ClientsSortCriterion;

import java.util.Date;
import java.util.List;

public interface ClientService {
    boolean addResident(Client resident, Date arrival, Date departure);

    boolean removeResident(Client resident);

    List<Client> getSortedClients(ClientsSortCriterion criterion);

    Integer getNumberOfResidents();

    Client getClientByPass(Integer passNummber);

    List<Client> getLast3Residents(Integer roomNumber);
}

