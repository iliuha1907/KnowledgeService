package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class HibernateAbstractDao<T> implements HibernateDao<T> {

    protected final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

    public abstract Class<T> getEntityClass();

    @Override
    public void add(final T object, final EntityManager entityManager) {
        try {
            entityManager.persist(object);
        } catch (Exception ex) {
            logger.error("Error at adding: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<T> getAll(final EntityManager entityManager) {
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
    public T getById(final Integer id, final EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
            Root<T> root = query.from(getEntityClass());
            query.select(root).where(criteriaBuilder.equal(root.get("id"), id));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException noResultException) {
            logger.error("Error at getting by id: no such entity!");
            return null;
        } catch (Exception ex) {
            logger.error("Error at getting by id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public void updateById(final Integer id, final Object value, final String columnName,
                           final EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<T> update = criteriaBuilder.createCriteriaUpdate(getEntityClass());
            Root<T> root = update.from(getEntityClass());
            update.set(columnName, value);
            update.where(criteriaBuilder.equal(root.get("id"), id));
            entityManager.createQuery(update).executeUpdate();
        } catch (Exception ex) {
            logger.error("Error at updating by id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }
}
