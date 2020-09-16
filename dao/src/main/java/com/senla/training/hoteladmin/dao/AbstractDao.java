package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.AbstractEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public abstract class AbstractDao<T extends AbstractEntity> implements GenericDao<T> {

    protected final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public void add(final T object) {
        try {
            entityManager.persist(object);
        } catch (Exception ex) {
            logger.error("Error at adding: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<T> getAll() {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
            Root<T> root = query.from(getEntityClass());
            query.select(root);
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting all: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public T getById(final Integer id) {
        try {
            T entity = entityManager.find(getEntityClass(), id);
            if (entity == null) {
                logger.error("Error at getting by id: no such entity!");
            }
            return entity;
        } catch (Exception ex) {
            logger.error("Error at getting by id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public void update(final T object) {
        try {
            entityManager.merge(object);
        } catch (Exception ex) {
            logger.error("Error at updating by attribute: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    protected abstract Class<T> getEntityClass();
}
