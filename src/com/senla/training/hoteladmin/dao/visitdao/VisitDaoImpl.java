package com.senla.training.hoteladmin.dao.visitdao;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.database.ClientFields;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.util.database.HotelServiceFields;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.database.VisitFields;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class VisitDaoImpl extends AbstractDao<Visit> implements VisitDao {
    @ConfigProperty(propertyName = "db.Visit.tableName", type = String.class)
    private static String tableName;
    @ConfigProperty(propertyName = "db.HotelService.tableName", type = String.class)
    private static String hotelServiceTable;
    @ConfigProperty(propertyName = "db.Client.tableName", type = String.class)
    private static String clientTable;
    private static final String fields = "(" + VisitFields.client_id + ", "
            + VisitFields.hotel_service_id + ", " + VisitFields.date + ", " + VisitFields.is_active + ")";
    private static final String jokers = "(?, ?, ?, ?)";

    @Override
    public List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion,
                                             Connection connection) {
        String sql = getSelectAllActiveQuery() + " and " + VisitFields.client_id + " = ? order by " + criterion;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            return parseSelectResultSet(statement.executeQuery());
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Visit getById(Integer id, Connection connection) {
        throw new BusinessException("Could not get by id");
    }

    @Override
    public void updateById(Integer id, Object value, String columnName, Connection connection) {
        throw new BusinessException("Could not update by id");
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
    protected String getSelectAllQuery() {
        return "select * from " + tableName + ", " + clientTable + ", " + hotelServiceTable
                + " where hotel_service_id = " + hotelServiceTable + ".id" + " and " +
                clientTable + ".id = client_id";
    }

    @Override
    protected List<Visit> parseSelectResultSet(ResultSet rs) {
        List<Visit> visits = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer serviceId = rs.getInt(VisitFields.hotel_service_id.toString());
                BigDecimal servicePrice = rs.getBigDecimal(HotelServiceFields.price.toString());
                HotelServiceType serviceType = HotelServiceType.valueOf(rs.getString(HotelServiceFields.type.toString()));
                String clientFirstName = rs.getString(ClientFields.first_name.toString());
                String clientLastName = rs.getString(ClientFields.last_name.toString());
                Integer clientId = rs.getInt(VisitFields.client_id.toString());
                Date date = rs.getDate(VisitFields.date.toString());
                boolean isActive = rs.getBoolean(VisitFields.is_active.toString());
                visits.add(new Visit(new Client(clientId, clientFirstName, clientLastName),
                        new HotelService(serviceId, servicePrice, serviceType), date, isActive));
            }
        } catch (SQLException ex) {
            throw new BusinessException(ex.getMessage());
        }
        return visits;
    }

    @Override
    protected List<Object> getInsertData(Visit object) {
        return new ArrayList<>(Arrays.asList(object.getClient().getId(), object.getHotelService().getId(),
                object.getDate(), object.isActive()));
    }

    private String getSelectAllActiveQuery() {
        return getSelectAllQuery() + " and " + VisitFields.is_active + " = 1";
    }
}

