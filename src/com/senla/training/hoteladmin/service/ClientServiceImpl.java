package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.dao.DaoManager;
import com.senla.training.hoteladmin.dao.clientdao.ClientDao;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;

import java.sql.Connection;
import java.util.List;

@NeedInjectionClass
public class ClientServiceImpl implements ClientService {
    @NeedInjectionField
    private ClientDao clientDao;
    @NeedInjectionField
    private DaoManager daoManager;

    @Override
    public void addClient(String firstName, String lastName) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            clientDao.add(new Client(firstName, lastName), connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            daoManager.setConnectionAutocommit(isAutocommit);
            throw ex;
        }
        daoManager.setConnectionAutocommit(isAutocommit);
    }

    @Override
    public List<Client> getClients() {
        return clientDao.getAll(daoManager.getConnection());
    }
}

