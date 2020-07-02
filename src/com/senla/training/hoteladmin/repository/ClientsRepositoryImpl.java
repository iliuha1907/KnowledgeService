package com.senla.training.hotelAdmin.repository;

import com.senla.training.hotelAdmin.exception.BusinessException;
import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.util.ClientIdProvider;

import java.util.ArrayList;
import java.util.List;

public class ClientsRepositoryImpl implements ClientsRepository {
    private static ClientsRepositoryImpl instance;
    private List<Client> clients;
    private List<Client> movedClients;

    private ClientsRepositoryImpl() {
        clients = new ArrayList<>();
        movedClients = new ArrayList<>();
    }

    public static ClientsRepository getInstance() {
        if (instance == null) {
            instance = new ClientsRepositoryImpl();
        }
        return instance;
    }

    @Override
    public List<Client> getClients() {
        return clients;
    }

    @Override
    public List<Client> getLastRoomClients(Integer roomId, Integer count) {
        List<Client> resultClients = new ArrayList<>();
        movedClients.forEach(client -> {
            if (client.getRoom().getId().equals(roomId)) {
                resultClients.add(client);
            }
        });
        return resultClients;
    }

    @Override
    public void addClient(Client client) {
        client.setId(ClientIdProvider.getNextId());
        clients.add(client);
    }

    @Override
    public void addMovedClient(Client client) {
        movedClients.add(client);
    }

    @Override
    public void removeClient(Client client) {
        if (!clients.remove(client)) {
            throw new BusinessException("Error at removing client: no such client!");
        }
    }

    @Override
    public Client getClientById(Integer id) {
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        return null;
    }

}

