package com.senla.training.hoteladmin.dao.reservationdao;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.database.ClientFields;
import com.senla.training.hoteladmin.util.database.ReservationFields;
import com.senla.training.hoteladmin.util.database.RoomFields;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationDaoImpl extends AbstractDao<Reservation> implements ReservationDao {
    @ConfigProperty(propertyName = "db.Reservation.tableName", type = String.class)
    private static String tableName;
    @ConfigProperty(propertyName = "db.Room.tableName", type = String.class)
    private static String roomTable;
    @ConfigProperty(propertyName = "db.Client.tableName", type = String.class)
    private static String clientTable;
    private static final String fields = "(" + ReservationFields.room_id +
            ", " + ReservationFields.resident_id + ", " + ReservationFields.arrival_date + ", " +
            ReservationFields.departure_date + ", " + ReservationFields.is_active + ")";
    private static final String jokers = "(?, ?, ?, ?, ?)";

    @Override
    public List<Reservation> getSortedReservations(Connection connection, ReservationSortCriterion criterion) {
        String sql = getSelectAllActiveQuery() + " order by " + criterion;
        try (Statement statement = connection.createStatement()) {
            return parseSelectResultSet(statement.executeQuery(sql));
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Integer getNumberOfResidents(Connection connection) {
        String sql = "select count(*) from " + tableName + " where " + ReservationFields.is_active + " = 1";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Reservation> getVisitsExpiredAfterDate(Date date, Connection connection) {
        String sql = getSelectAllActiveQuery() + " and " + ReservationFields.departure_date + " < ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(DateUtil.getString(date)));
            return parseSelectResultSet(statement.executeQuery());
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Reservation> getLastRoomVisits(Integer roomId, Integer count, Connection connection) {
        String sql = getSelectAllQuery() + " and " + ReservationFields.room_id + " = ? limit ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setInt(2, count);
            return parseSelectResultSet(statement.executeQuery());
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Reservation getClientReservation(Integer clientId, Connection connection) {
        String sql = getSelectAllActiveQuery() + " and " + ReservationFields.resident_id + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            List<Reservation> reservations = parseSelectResultSet(statement.executeQuery());
            return reservations.size() == 0 ? null : reservations.get(0);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public void deactivateClientReservation(Integer clientId, Connection connection) {
        String sql = getUpdateQuery(ReservationFields.is_active.toString()) + " where " +
                ReservationFields.resident_id + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, false);
            statement.setInt(2, clientId);
            statement.execute();
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Reservation getById(Integer id, Connection connection) {
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
    protected List<Reservation> parseSelectResultSet(ResultSet rs) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer roomId = rs.getInt(ReservationFields.room_id.toString());
                RoomStatus roomStatus = RoomStatus.valueOf(rs.getString(RoomFields.status.toString()));
                BigDecimal roomPrice = rs.getBigDecimal(RoomFields.price.toString());
                Integer roomCapacity = rs.getInt(RoomFields.capacity.toString());
                Integer roomStars = rs.getInt(RoomFields.stars.toString());

                Integer clientId = rs.getInt(ReservationFields.resident_id.toString());
                String clientFirstName = rs.getString(ClientFields.first_name.toString());
                String clientLastName = rs.getString(ClientFields.last_name.toString());
                Date arrival_date = rs.getDate(ReservationFields.arrival_date.toString());
                Date departure_date = rs.getDate(ReservationFields.departure_date.toString());

                boolean isFree = rs.getBoolean(RoomFields.is_free.toString());
                boolean isActive = rs.getBoolean(ReservationFields.is_active.toString());

                reservations.add(new Reservation(new Room(roomId, roomStatus, roomPrice, roomCapacity, roomStars, isFree),
                        new Client(clientId, clientFirstName, clientLastName),
                        arrival_date, departure_date, isActive));
            }
        } catch (SQLException ex) {
            throw new BusinessException(ex.getMessage());
        }
        return reservations;
    }

    @Override
    protected String getSelectAllQuery() {
        return "select *" + " from " +
                tableName + ", " + clientTable + ", " + roomTable + " where " + ReservationFields.room_id + " = " +
                roomTable + ".id" + " and " + clientTable + ".id = " + ReservationFields.resident_id;
    }

    @Override
    protected List<Object> getInsertData(Reservation object) {
        return new ArrayList<>(Arrays.asList(object.getRoom().getId(), object.getResident().getId(),
                object.getArrivalDate(), object.getDepartureDate(), object.isActive()));
    }

    private String getSelectAllActiveQuery() {
        return getSelectAllQuery() + " and " + ReservationFields.is_active + " = 1";
    }
}

