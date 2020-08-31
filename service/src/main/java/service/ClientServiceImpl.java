package service;

import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import dao.DaoManager;
import dao.client.ClientDao;
import filecsv.writeread.ClientReaderWriter;
import model.client.Client;

import java.sql.Connection;
import java.util.List;

@NeedInjectionClass
public class ClientServiceImpl implements ClientService {

    @NeedInjectionField
    private ClientDao clientDao;
    @NeedInjectionField
    private DaoManager daoManager;

    @Override
    public void addClient(final String firstName, final String lastName) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            clientDao.add(new Client(firstName, lastName), connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public List<Client> getClients() {
        return clientDao.getAll(daoManager.getConnection());
    }

    @Override
    public Integer getNumberOfClients() {
        return clientDao.getNumberOfClients(daoManager.getConnection());
    }

    @Override
    public void exportClients() {
        ClientReaderWriter.writeClients(clientDao.getAll(daoManager.getConnection()));
    }

    @Override
    public void importClients() {
        List<Client> clients = ClientReaderWriter.readClients();
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            clients.forEach(client -> {
                Client existing = clientDao.getById(client.getId(), connection);
                if (existing == null) {
                    clientDao.add(client, connection);
                }
            });
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }
}

