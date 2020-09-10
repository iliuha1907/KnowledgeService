package com.senla.training.hoteladmin.service.client;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionField;
import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.csvapi.writeread.ClientReaderWriter;
import com.senla.training.hoteladmin.model.client.Client;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

@NeedInjectionClass
public class ClientServiceImpl implements ClientService {

    @NeedInjectionField
    private ClientDao clientDao;

    @Override
    public void addClient(String firstName, String lastName) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            clientDao.add(new Client(firstName, lastName), entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public List<Client> getClients() {
        return clientDao.getAll(EntityManagerProvider.getEntityManager());
    }

    @Override
    public Long getNumberOfClients() {
        return clientDao.getNumberOfClients(EntityManagerProvider.getEntityManager());
    }

    @Override
    public void exportClients() {
        ClientReaderWriter.writeClients(clientDao.getAll(EntityManagerProvider.getEntityManager()));
    }

    @Override
    public void importClients() {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        List<Client> clients = ClientReaderWriter.readClients();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            clients.forEach(client -> {
                clientDao.add(client, entityManager);
            });
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }
}
