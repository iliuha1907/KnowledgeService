package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.annotationapi.ConfigProperty;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@NeedInjectionClass
public class EntityManagerProvider {

    private static final Logger LOGGER = LogManager.getLogger(EntityManagerProvider.class);
    @ConfigProperty(propertyName = "dao.entityManager.entityManagerName", type = String.class)
    private static String entityManagerName;
    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;

    public static EntityManager getEntityManager() {
        try {
            if (entityManager == null || !entityManager.isOpen()) {
                if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
                    entityManagerFactory = Persistence.createEntityManagerFactory(entityManagerName);
                }
                entityManager = entityManagerFactory.createEntityManager();
            }
            return entityManager;
        } catch (Exception ex) {
            LOGGER.error("Error at getting entity manager: " + ex.getMessage());
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

    public static void close() {
        try {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        } catch (Exception ex) {
            LOGGER.error("Error at closing entity manager: " + ex.getMessage());
            throw new IncorrectWorkException(ex.getMessage());
        }
    }
}
