package dao.client;

import dao.GenericDao;
import model.client.Client;

import java.sql.Connection;

public interface ClientDao extends GenericDao<Client> {

    Integer getNumberOfClients(Connection connection);
}

