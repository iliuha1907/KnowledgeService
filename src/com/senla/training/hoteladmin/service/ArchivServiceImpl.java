package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repo.ClientsArchiveRepo;
import com.senla.training.hoteladmin.model.client.Client;

import java.util.LinkedList;
import java.util.List;

public class ArchivServiceImpl implements ArchivService {
    private static ArchivServiceImpl instance;
    private ClientsArchiveRepo archive;

    private ArchivServiceImpl(ClientsArchiveRepo archive) {
        this.archive = archive;
    }

    public static ArchivServiceImpl getInstance(ClientsArchiveRepo archive) {
        if(instance == null){
            instance = new ArchivServiceImpl(archive);
            return instance;
        }
        return instance;
    }

    public void addClient(Client client) {
        List<Client> clients = archive.getClients();
        clients.add(client);
        archive.setClients(clients);
    }

    public List<Client> getLast3Residents(Integer roomNumber) {
        List<Client> clients = archive.getClients();
        List<Client> residents = new LinkedList<>();
         int times = 3;
        for(Client client:clients){
            if (client.getRoom().getNumber().equals(roomNumber)) {
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

