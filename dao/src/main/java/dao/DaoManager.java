package dao;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import exception.IncorrectWorkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NeedInjectionClass
public class DaoManager {

    @ConfigProperty(propertyName = "daoManager.driver", type = String.class)
    private String driverName;
    private static final Logger LOGGER = LogManager.getLogger(DaoManager.class);
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
            LOGGER.error("Error at getting connection: " + ex.getMessage());
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
            LOGGER.error("Error at closing connection: " + ex.getMessage());
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

    public boolean getAutoConnectionCommit() {
        try {
            return connection.getAutoCommit();
        } catch (SQLException ex) {
            LOGGER.error("Error at getting auto commit of connection: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public void setConnectionAutocommit(final boolean value) {
        try {
            connection.setAutoCommit(value);
        } catch (SQLException ex) {
            LOGGER.error("Error at setting auto commit of connection: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public void connectionCommit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            LOGGER.error("Error at committing connection: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            LOGGER.error("Error at rollback of connection: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    private void openConnection() {
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception ex) {
            LOGGER.error("Error at opening connection: " + ex.getMessage());
            throw new IncorrectWorkException(ex.getMessage());
        }
    }
}

