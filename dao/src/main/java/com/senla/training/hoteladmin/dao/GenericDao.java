package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.model.AbstractEntity;

import java.util.List;

public interface GenericDao<T extends AbstractEntity, PK> {

    void add(T object);

    List<T> getAll();

    T getByPrimaryKey(PK key);

    void update(T object);
}
