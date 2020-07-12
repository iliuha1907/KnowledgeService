package com.senla.training.hoteladmin.repository;


import com.senla.training.hoteladmin.annotation.NeedDiClass;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.idspread.ClientIdProvider;

import java.util.ArrayList;
import java.util.List;

@NeedDiClass
public class ClientsRepositoryImpl implements ClientsRepository {
    private List<Client> clients;
    private List<Client> movedClients;

    public ClientsRepositoryImpl() {
        clients = new ArrayList<>();
        movedClients = new ArrayList<>();
    }

    @Override
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    @Override
    public void setMovedClients(List<Client> clients) {
        this.movedClients = clients;
    }

    @Override
    public List<Client> getClients() {
        return clients;
    }

    @Override
    public List<Client> getMovedClients() {
        return movedClients;
    }

    @Override
    public List<Client> getLastRoomClients(Integer roomId, Integer count) {
        List<Client> resultClients = new ArrayList<>();
        Integer times = count;
        for (Client client : movedClients) {
            if (client.getRoom().getId().equals(roomId)) {
                if (times == 0) {
                    break;
                }
                resultClients.add(client);
                times--;
            }
        }
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
        try {
            return clients.stream()
                    .filter(client -> client.getId().equals(id))
                    .findFirst()
                    .get();
        } catch (Exception ex) {
            return null;
        }
    }
}

