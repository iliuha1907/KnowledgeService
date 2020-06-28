package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repository.ClientsArchiveRepository;
import com.senla.training.hoteladmin.model.client.Client;

import java.util.LinkedList;
import java.util.List;

public class ArchivServiceImpl implements ArchivService {
    private final Integer LAST_CLIENTS = 3;
    private static ArchivServiceImpl instance;
    private ClientsArchiveRepository archive;

    private ArchivServiceImpl(ClientsArchiveRepository archive) {
        this.archive = archive;
    }

    public static ArchivService getInstance(ClientsArchiveRepository archive) {
        if(instance == null){
            instance = new ArchivServiceImpl(archive);
            return instance;
        }
        return instance;
    }

    @Override
    public void addClient(Client client) {
        List<Client> clients = archive.getClients();
        clients.add(client);
        archive.setClients(clients);
    }

    @Override
    public List<Client> getLastThreeResidents(Integer roomId) {
        List<Client> clients = archive.getClients();
        List<Client> residents = new LinkedList<>();

        Integer times =LAST_CLIENTS;
        for(Client client:clients){
            if (client.getRoom().getId().equals(roomId)) {
                residents.add(client);
                times--;
                if (times == 0) {
                    break;
                }
            }
        }
      return residents;
    }
}

