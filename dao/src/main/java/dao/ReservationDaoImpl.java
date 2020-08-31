package dao;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import model.client.Client;
import model.reservation.Reservation;
import model.room.Room;
import model.room.RoomStatus;
import util.DateUtil;
import util.database.ClientField;
import util.database.ReservationField;
import util.database.RoomField;
import util.sort.ReservationSortCriterion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationDaoImpl extends AbstractDao<Reservation> implements ReservationDao {

    private static final String FIELDS = "(" + ReservationField.ROOM_ID.toString().toLowerCase()
            + ", " + ReservationField.RESIDENT_ID.toString().toLowerCase()
            + ", " + ReservationField.ARRIVAL_DATE.toString().toLowerCase() + ", "
            + ReservationField.DEPARTURE_DATE.toString().toLowerCase()
            + ", " + ReservationField.IS_ACTIVE.toString().toLowerCase() + ")";
    private static final String JOKERS = "(?, ?, ?, ?, ?)";
    @ConfigProperty(propertyName = "db.Reservation.tableName", type = String.class)
    private static String tableName;
    @ConfigProperty(propertyName = "db.Room.tableName", type = String.class)
    private static String roomTable;
    @ConfigProperty(propertyName = "db.Client.tableName", type = String.class)
    private static String clientTable;

    @Override
    public List<Reservation> getSortedReservations(final Connection connection,
                                                   final ReservationSortCriterion criterion) {
        String sql = getSelectAllActiveQuery() + " order by " + criterion;
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            logger.error("Error at getting sorted reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Integer getNumberOfResidents(final Connection connection) {
        String innerSql = "select distinct " + ReservationField.RESIDENT_ID.toString().toLowerCase()
                + " from " + tableName + " where " + ReservationField.IS_ACTIVE.toString().toLowerCase()
                + " = 1";
        String sql = "select count(*) from (" + innerSql + ") as residents";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception ex) {
            logger.error("Error at getting number of residents: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Reservation> getVisitsExpiredAfterDate(final Date date, final Connection connection) {
        String sql = getSelectAllActiveQuery() + " and "
                + ReservationField.DEPARTURE_DATE.toString().toLowerCase() + " < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(DateUtil.getString(date)));
            return parseSelectResultSet(statement.executeQuery());
        } catch (Exception ex) {
            logger.error("Error at getting visits expired after date: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Reservation> getLastRoomVisits(final Integer roomId, final Integer count,
                                               final Connection connection) {
        String sql = getSelectAllQuery() + " and " + ReservationField.ROOM_ID.toString().toLowerCase()
                + " = ? limit ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setInt(2, count);
            return parseSelectResultSet(statement.executeQuery());
        } catch (Exception ex) {
            logger.error("Error at getting last room residents: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Reservation getClientReservation(final Integer clientId, final Connection connection) {
        String sql = getSelectAllActiveQuery() + " and "
                + ReservationField.RESIDENT_ID.toString().toLowerCase() + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            List<Reservation> reservations = parseSelectResultSet(statement.executeQuery());
            return reservations.size() == 0 ? null : reservations.get(0);
        } catch (Exception ex) {
            logger.error("Error at getting client reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public void deactivateClientReservation(final Integer clientId, final Connection connection) {
        String sql = getUpdateQuery(ReservationField.IS_ACTIVE.toString().toLowerCase()) + " where "
                + ReservationField.RESIDENT_ID.toString().toLowerCase() + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setInt(2, clientId);
            statement.execute();
        } catch (Exception ex) {
            logger.error("Error at deactivating client reservations: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Reservation getByRoomClientId(final Integer roomId, final Integer clientId, final Connection connection) {
        String sql = getSelectAllActiveQuery() + " and "
                + ReservationField.ROOM_ID.toString().toLowerCase() + " = ? and "
                + ReservationField.RESIDENT_ID.toString().toLowerCase() + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setInt(2, clientId);
            List<Reservation> reservations = parseSelectResultSet(statement.executeQuery());
            return reservations.size() == 0 ? null : reservations.get(0);
        } catch (Exception ex) {
            logger.error("Error at getting reservation by room and client id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Reservation getById(final Integer id, final Connection connection) {
        logger.error("Error at getting reservation by id: Could not get by id");
        throw new BusinessException("Could not get by id");
    }

    @Override
    public void updateById(final Integer id, final Object value, final String columnName,
                           final Connection connection) {
        logger.error("Error at updating reservation by id: Could not update by id");
        throw new BusinessException("Could not update by id");
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
    protected String getSelectAllQuery() {
        return "select * from "
                + tableName + ", " + clientTable + ", " + roomTable + " where "
                + ReservationField.ROOM_ID.toString().toLowerCase() + " = "
                + roomTable + ".id" + " and " + clientTable + ".id = "
                + ReservationField.RESIDENT_ID.toString().toLowerCase();
    }

    @Override
    protected List<Reservation> parseSelectResultSet(final ResultSet rs) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer roomId = rs.getInt(ReservationField.ROOM_ID.toString().toLowerCase());
                RoomStatus roomStatus = RoomStatus.valueOf(rs.getString(
                        RoomField.STATUS.toString().toLowerCase()));
                BigDecimal roomPrice = rs.getBigDecimal(RoomField.PRICE.toString().toLowerCase());
                Integer roomCapacity = rs.getInt(RoomField.CAPACITY.toString().toLowerCase());
                Integer roomStars = rs.getInt(RoomField.STARS.toString().toLowerCase());

                Integer clientId = rs.getInt(ReservationField.RESIDENT_ID.toString().toLowerCase());
                String clientFirstName = rs.getString(ClientField.FIRST_NAME.toString().toLowerCase());
                String clientLastName = rs.getString(ClientField.LAST_NAME.toString().toLowerCase());
                Date arrivalDate = rs.getDate(ReservationField.ARRIVAL_DATE.toString().toLowerCase());
                Date departureDate = rs.getDate(ReservationField.DEPARTURE_DATE.toString().toLowerCase());

                boolean isFree = rs.getBoolean(RoomField.IS_FREE.toString().toLowerCase());
                boolean isActive = rs.getBoolean(ReservationField.IS_ACTIVE.toString().toLowerCase());

                reservations.add(new Reservation(new Room(roomId, roomStatus, roomPrice, roomCapacity, roomStars, isFree),
                        new Client(clientId, clientFirstName, clientLastName),
                        arrivalDate, departureDate, isActive));
            }
        } catch (SQLException ex) {
            logger.error("Error at parsing select result: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
        return reservations;
    }

    @Override
    protected List<Object> getInsertData(final Reservation object) {
        return Arrays.asList(object.getRoom().getId(), object.getResident().getId(),
                object.getArrivalDate(), object.getDepartureDate(), object.isActive());
    }

    private String getSelectAllActiveQuery() {
        return getSelectAllQuery() + " and " + ReservationField.IS_ACTIVE.toString().toLowerCase() + " = 1";
    }
}

