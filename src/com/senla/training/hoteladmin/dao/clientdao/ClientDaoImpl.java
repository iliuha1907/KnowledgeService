package com.senla.training.hoteladmin.dao.clientdao;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.database.ClientFields;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.senla.training.hoteladmin.util.database.ClientFields.last_name;

@NeedInjectionClass
public class ClientDaoImpl extends AbstractDao<Client> implements ClientDao {
    @ConfigProperty(propertyName = "db.Client.tableName", type = String.class)
    private static String tableName;
    private static final String fields = "(" + ClientFields.first_name + ", " + last_name + ")";
    private static final String jokers = "(?, ?)";

    @Override
    public Integer getNumberOfClients(Connection connection) {
        String sql = "select count(*) from " + tableName;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected String getFields() {
        return fields;
    }

    @Override
    protected String getInsertJokers() {
        return jokers;
    }

    @Override
    protected List<Client> parseSelectResultSet(ResultSet rs) {
        List<Client> clients = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer id = rs.getInt(ClientFields.id.toString());
                String firstName = rs.getString(ClientFields.first_name.toString());
                String lastName = rs.getString(ClientFields.last_name.toString());
                clients.add(new Client(id, firstName, lastName));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return clients;
    }

    @Override
    protected List<Object> getInsertData(Client object) {
        return new ArrayList<>(Arrays.asList(object.getFirstName(), object.getLastName()));
    }
}

