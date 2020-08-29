package com.senla.training.hoteladmin.dao.clientdao;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.client.Client;

import java.sql.Connection;

public interface ClientDao extends GenericDao<Client> {

    Integer getNumberOfClients(Connection connection);
}

