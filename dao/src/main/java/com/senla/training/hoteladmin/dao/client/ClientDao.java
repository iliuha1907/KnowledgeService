package com.senla.training.hoteladmin.dao.client;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.client.Client;

import javax.persistence.EntityManager;

public interface ClientDao extends GenericDao<Client> {

    Long getNumberOfClients(EntityManager entityManager);
}
