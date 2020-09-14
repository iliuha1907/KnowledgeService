package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.model.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

public interface GenericDao<T extends AbstractEntity> {

    void add(T object, EntityManager entityManager);

    List<T> getAll(EntityManager entityManager);

    T getById(Integer id, EntityManager entityManager);

    <X, Y> void updateByAttribute(X key, SingularAttribute<? super T, X> keyAttribute,
                                  Y value, SingularAttribute<? super T, Y> valueAttribute,
                                  EntityManager entityManager);
}
