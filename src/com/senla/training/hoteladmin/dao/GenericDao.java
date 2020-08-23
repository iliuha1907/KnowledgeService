package com.senla.training.hoteladmin.dao;

import java.sql.Connection;
import java.util.List;

public interface GenericDao<T> {

    void add(T object, Connection connection);

    List<T> getAll(Connection connection);

    T getById(Integer id, Connection connection);

    void updateById(Integer id, Object value, String columnName, Connection connection);
}

