package dao;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import model.room.Room;
import model.room.RoomStatus;
import util.database.RoomField;
import util.sort.RoomsSortCriterion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NeedInjectionClass
public class RoomDaoImpl extends AbstractDao<Room> implements RoomDao {

    private static final String FIELDS = "(" + RoomField.STATUS.toString().toLowerCase()
            + ", " + RoomField.PRICE.toString().toLowerCase() + ", "
            + RoomField.CAPACITY.toString().toLowerCase() + ", "
            + RoomField.STARS.toString().toLowerCase() + ", "
            + RoomField.IS_FREE.toString().toLowerCase() + ")";
    private static final String JOKERS = "(?, ?, ?, ?, ?)";
    @ConfigProperty(propertyName = "db.Room.tableName", type = String.class)
    private static String tableName;

    @Override
    public List<Room> getSortedRooms(final RoomsSortCriterion criterion, final Connection connection) {
        String sql = getSelectAllQuery() + " order by " + criterion;
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            logger.error("Error at getting sorted rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public List<Room> getFreeRooms(final Connection connection) {
        String sql = getSelectFreeRoomsQuery();
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            logger.error("Error at getting free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Room> getSortedFreeRooms(final RoomsSortCriterion criterion, final Connection connection) {
        String sql = getSelectFreeRoomsQuery() + " order by " + criterion;
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            logger.error("Error at getting sorted free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Integer getNumberOfFreeRooms(final Connection connection) {
        String sql = "select count(*) from " + tableName + " where "
                + RoomField.IS_FREE.toString().toLowerCase() + " = 1 and "
                + RoomField.STATUS.toString().toLowerCase() + " = 'served'";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception ex) {
            logger.error("Error at getting number of free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Room getFirstFreeRoom(final Connection connection) {
        String sql = getSelectFreeRoomsQuery() + " limit 1";
        try (Statement statement = connection.createStatement()) {
            List<Room> rooms = parseSelectResultSet(statement.executeQuery(sql));
            return rooms.size() == 0 ? null : rooms.get(0);
        } catch (Exception ex) {
            logger.error("Error at getting first free room: " + ex.getMessage());
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
    protected List<Room> parseSelectResultSet(final ResultSet rs) {
        List<Room> rooms = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer id = rs.getInt(RoomField.ID.toString().toLowerCase());
                RoomStatus status = RoomStatus.valueOf(rs.getString(
                        RoomField.STATUS.toString().toLowerCase()));
                BigDecimal price = rs.getBigDecimal(RoomField.PRICE.toString().toLowerCase());
                Integer capacity = rs.getInt(RoomField.CAPACITY.toString().toLowerCase());
                Integer stars = rs.getInt(RoomField.STARS.toString().toLowerCase());
                boolean isFree = rs.getBoolean(RoomField.IS_FREE.toString().toLowerCase());
                rooms.add(new Room(id, status, price, capacity, stars, isFree));
            }
        } catch (SQLException ex) {
            logger.error("Error at parsing select result: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
        return rooms;
    }

    @Override
    protected List<Object> getInsertData(final Room object) {
        return Arrays.asList(object.getStatus().toString(), object.getPrice(),
                object.getCapacity(), object.getStars(), object.isFree());
    }

    private String getSelectFreeRoomsQuery() {
        return "select * from " + tableName + " where " + RoomField.IS_FREE.toString().toLowerCase()
                + " = 1 and " + RoomField.STATUS.toString().toLowerCase() + " = 'served'";
    }
}

