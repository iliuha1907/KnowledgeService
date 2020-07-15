package com.senla.training.hoteladmin.repository;

import com.senla.training.injection.annotation.InterfaceOfInjection;
import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

@InterfaceOfInjection
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

