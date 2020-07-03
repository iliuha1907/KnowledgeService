package com.senla.training.hotelAdmin.repository;

import com.senla.training.hotelAdmin.model.client.Client;

import java.util.List;

public interface ClientsRepository {

    void setClients(List<Client> clients);

    void setMovedClients(List<Client> clients);

    List<Client> getClients();

    List<Client> getMovedClients();

    List<Client> getLastRoomClients(Integer roomId, Integer count);

    void addClient(Client client);

    void addMovedClient(Client client);

    void removeClient(Client client);

    Client getClientById(Integer id);
}

