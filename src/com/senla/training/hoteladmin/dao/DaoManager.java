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
    private static Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            openConnection();
        }
        try {
            if (connection.isClosed()) {
                openConnection();
            }
        } catch (SQLException ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
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

    private void openConnection() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

}

