package com.senla.training.hoteladmin.service.client;

import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.csvapi.writeread.ClientReaderWriter;
import com.senla.training.hoteladmin.model.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;
    @Autowired
    private EntityManagerProvider entityManagerProvider;
    @Autowired
    private ClientReaderWriter clientReaderWriter;

    @Override
    public void addClient(String firstName, String lastName) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        try {
            clientDao.add(new Client(firstName, lastName), entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<Client> getClients() {
        return clientDao.getAll(entityManagerProvider.getEntityManager());
    }

    @Override
    public Long getNumberOfClients() {
        return clientDao.getNumberOfClients(entityManagerProvider.getEntityManager());
    }

    @Override
    public void exportClients() {
        clientReaderWriter.writeClients(clientDao.getAll(entityManagerProvider.getEntityManager()));
    }

    @Override
    public void importClients() {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        List<Client> clients = clientReaderWriter.readClients();
        try {
            clients.forEach(client -> {
                clientDao.add(client, entityManager);
            });
        } catch (Exception ex) {
            throw ex;
        }
    }
}
