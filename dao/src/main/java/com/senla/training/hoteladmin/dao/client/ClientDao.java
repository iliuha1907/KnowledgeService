package com.senla.training.hoteladmin.dao.client;

import com.senla.training.hoteladmin.dao.HibernateDao;
import com.senla.training.hoteladmin.model.client.Client;

import javax.persistence.EntityManager;

public interface ClientDao extends HibernateDao<Client> {

    Long getNumberOfClients(EntityManager entityManager);
}
