package com.senla.training.hoteladmin.dao.client;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
public class ClientDaoImpl extends AbstractDao<Client> implements ClientDao {

    @Override
    public Class<Client> getEntityClass() {
        return Client.class;
    }

    @Override
    public Long getNumberOfClients(EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Client> root = query.from(Client.class);
            query.select(criteriaBuilder.count(root));
            return entityManager.createQuery(query).getSingleResult();
        } catch (Exception ex) {
            logger.error("Error at getting number of clients: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }
}
