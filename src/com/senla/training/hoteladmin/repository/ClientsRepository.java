package com.senla.training.hotelAdmin.repository;

import com.senla.training.hotelAdmin.model.client.Client;

import java.util.List;

public interface ClientsRepository {
    List<Client> getClients();

    List<Client> getLastRoomClients(Integer roomId, Integer count);

    void addClient(Client client);

    void addMovedClient(Client client);

    void removeClient(Client client);

    Client getClientById(Integer id);
}

