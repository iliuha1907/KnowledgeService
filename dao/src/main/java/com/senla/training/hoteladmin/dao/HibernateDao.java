package com.senla.training.hoteladmin.dao;

import javax.persistence.EntityManager;
import java.util.List;

public interface HibernateDao<T> {

    void add(T object, EntityManager entityManager);

    List<T> getAll(EntityManager entityManager);

    T getById(Integer id, EntityManager entityManager);

    void updateById(Integer id, Object value, String columnName, EntityManager entityManager);
}
