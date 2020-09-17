package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.model.AbstractEntity;

import java.util.List;

public interface GenericDao<T extends AbstractEntity> {

    void add(T object);

    List<T> getAll();

    T getById(Integer id);

    void update(T object);
}
