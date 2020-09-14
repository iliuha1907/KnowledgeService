package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Component
public class EntityManagerProvider {

    private final Logger logger = LogManager.getLogger(EntityManagerProvider.class);
    @PersistenceContext
    private EntityManager entityManager;
    @PersistenceContext
    private EntityManagerFactory entityManagerFactory;

    public EntityManager getEntityManager() {
        if (entityManager == null || !entityManager.isOpen()) {
            logger.error("Error at getting entity manager: it was not initialized");
            throw new IncorrectWorkException("Error at getting entity manager: it was not initialized");
        }
        return entityManager;
    }

    public void close() {
        try {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        } catch (Exception ex) {
            logger.error("Error at closing entity manager: " + ex.getMessage());
            throw new IncorrectWorkException(ex.getMessage());
        }
    }
}
