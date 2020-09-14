package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.AbstractEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

public abstract class AbstractDao<T extends AbstractEntity> implements GenericDao<T> {

    protected final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

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
            return entityManager.find(getEntityClass(), id);
        } catch (NoResultException noResultException) {
            logger.error("Error at getting by id: no such entity!");
            return null;
        } catch (Exception ex) {
            logger.error("Error at getting by id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public <X, Y> void updateByAttribute(final X key, final SingularAttribute<? super T, X> keyAttribute,
                                         final Y value, final SingularAttribute<? super T, Y> valueAttribute,
                                         final EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<T> update = criteriaBuilder.createCriteriaUpdate(getEntityClass());
            Root<T> root = update.from(getEntityClass());
            update.set(root.get(valueAttribute), value);
            update.where(criteriaBuilder.equal(root.get(keyAttribute), key));
            entityManager.createQuery(update).executeUpdate();
        } catch (Exception ex) {
            logger.error("Error at updating by id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    protected abstract Class<T> getEntityClass();
}
