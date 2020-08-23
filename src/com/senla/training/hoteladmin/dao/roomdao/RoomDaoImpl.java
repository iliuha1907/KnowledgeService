package com.senla.training.hoteladmin.dao.roomdao;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.database.RoomFields;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NeedInjectionClass
public class RoomDaoImpl extends AbstractDao<Room> implements RoomDao {
    @ConfigProperty(propertyName = "db.Room.tableName", type = String.class)
    private static String tableName;
    private static final String fields = "(" + RoomFields.status + ", " + RoomFields.price + ", " +
            RoomFields.capacity + ", " + RoomFields.stars + ", " + RoomFields.isFree + ")";
    private static final String jokers = "(?, ?, ?, ?, ?)";

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion, Connection connection) {
        String sql = getSelectAllQuery() + " order by " + criterion;
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public List<Room> getFreeRooms(Connection connection) {
        String sql = getSelectFreeRoomsQuery();
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion, Connection connection) {
        String sql = getSelectFreeRoomsQuery() + " order by " + criterion;
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Integer getNumberOfFreeRooms(Connection connection) {
        String sql = "select count(*) from " + tableName + " where " + RoomFields.isFree + " = 1 and " +
                RoomFields.status + " = 'served'";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Room getFirstFreeRoom(Connection connection) {
        String sql = getSelectFreeRoomsQuery() + " limit 1";
        try (Statement statement = connection.createStatement()) {
            List<Room> rooms = parseSelectResultSet(statement.executeQuery(sql));
            return rooms.size() == 0 ? null : rooms.get(0);
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
    protected List<Room> parseSelectResultSet(ResultSet rs) {
        List<Room> rooms = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer id = rs.getInt(RoomFields.id.toString());
                RoomStatus status = RoomStatus.valueOf(rs.getString(RoomFields.status.toString()));
                BigDecimal price = rs.getBigDecimal(RoomFields.price.toString());
                Integer capacity = rs.getInt(RoomFields.capacity.toString());
                Integer stars = rs.getInt(RoomFields.stars.toString());
                boolean isFree = rs.getBoolean(RoomFields.isFree.toString());
                rooms.add(new Room(id, status, price, capacity, stars, isFree));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return rooms;
    }

    @Override
    protected List<Object> getInsertData(Room object) {
        return new ArrayList<>(Arrays.asList(object.getStatus().toString(), object.getPrice(),
                object.getCapacity(), object.getStars(), object.isFree()));
    }

    private String getSelectFreeRoomsQuery() {
        return "select * from " + tableName + " where " + RoomFields.isFree + " = 1 and "
                + RoomFields.status + " = 'served'";
    }
}

