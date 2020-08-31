package dao.client;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import dao.AbstractDao;
import exception.BusinessException;
import model.client.Client;
import util.database.ClientField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NeedInjectionClass
public class ClientDaoImpl extends AbstractDao<Client> implements ClientDao {

    private static final String FIELDS = "(" + ClientField.FIRST_NAME.toString().toLowerCase()
            + ", " + ClientField.LAST_NAME.toString().toLowerCase() + ")";
    private static final String JOKERS = "(?, ?)";
    @ConfigProperty(propertyName = "db.Client.tableName", type = String.class)
    private static String tableName;

    @Override
    public Integer getNumberOfClients(final Connection connection) {
        String sql = "select count(*) from " + tableName;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception ex) {
            logger.error("Error at getting number of clients: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected String getFields() {
        return FIELDS;
    }

    @Override
    protected String getInsertJokers() {
        return JOKERS;
    }

    @Override
    protected List<Client> parseSelectResultSet(final ResultSet rs) {
        List<Client> clients = new ArrayList<Client>();
        try {
            while (rs.next()) {
                Integer id = rs.getInt(ClientField.ID.toString().toLowerCase());
                String firstName = rs.getString(ClientField.FIRST_NAME.toString().toLowerCase());
                String lastName = rs.getString(ClientField.LAST_NAME.toString().toLowerCase());
                clients.add(new Client(id, firstName, lastName));
            }
        } catch (SQLException ex) {
            logger.error("Error at parsing select result: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
        return clients;
    }

    @Override
    protected List<Object> getInsertData(final Client object) {
        return Arrays.asList(object.getFirstName(), object.getLastName());
    }
}

