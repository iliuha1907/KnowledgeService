package com.senla.training.hoteladmin.service;

import com.senla.training.injection.annotation.InterfaceOfInjection;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;

import java.util.Date;
import java.util.List;

@InterfaceOfInjection
public interface ClientService {

    void setClients(List<Client> clients);

    void setLastResidents(List<Client> residents);

    void addResident(String firstName, String lastName, Date arrival, Date departure);

    void removeResident(Client resident);

    void removeResidentById(Integer id);

    List<Client> getSortedClients(ClientsSortCriterion criterion);

    Integer getNumberOfResidents();

    Client getClientById(Integer id);

    List<Client> getLastResidents(Integer roomId);

    void exportClients();

    void importClients();

    void updateClient(Client client);

    void serializeMovedClients();

    void deserializeMovedClients();

    void serializeId();

    void deserializeId();
}

