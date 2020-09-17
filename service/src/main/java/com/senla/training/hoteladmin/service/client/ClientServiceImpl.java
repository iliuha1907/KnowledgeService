package com.senla.training.hoteladmin.service.client;

import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.csvapi.writeread.ClientReaderWriter;
import com.senla.training.hoteladmin.model.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ClientReaderWriter clientReaderWriter;

    @Override
    @Transactional
    public void addClient(String firstName, String lastName) {
        clientDao.add(new Client(firstName, lastName));
    }

    @Override
    @Transactional
    public List<Client> getClients() {
        return clientDao.getAll();
    }

    @Override
    @Transactional
    public Long getNumberOfClients() {
        return clientDao.getNumberOfClients();
    }

    @Override
    @Transactional
    public void exportClients() {
        clientReaderWriter.writeClients(clientDao.getAll());
    }

    @Override
    @Transactional
    public void importClients() {
        List<Client> clients = clientReaderWriter.readClients();
        clients.forEach(client -> {
            clientDao.add(client);
        });
    }
}
