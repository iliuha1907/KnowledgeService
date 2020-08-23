package com.senla.training.hoteladmin.dao;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NeedInjectionClass
public class DaoManager {
    @ConfigProperty(propertyName = "daoManager.url", type = String.class)
    private String url;
    @ConfigProperty(propertyName = "daoManager.user", type = String.class)
    private String user;
    @ConfigProperty(propertyName = "daoManager.password", type = String.class)
    private String password;
    private Connection connection;

    public void openConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean getAutoConnectionCommit() {
        try {
            return connection.getAutoCommit();
        } catch (SQLException ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public void setConnectionAutocommit(boolean value) {
        try {
            connection.setAutoCommit(value);
        } catch (SQLException ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public void commitConnection() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new BusinessException(ex.getMessage());
        }
    }
}

