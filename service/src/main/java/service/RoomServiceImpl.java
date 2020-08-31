package service;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import dao.DaoManager;
import dao.RoomDao;
import exception.BusinessException;
import filecsv.writeread.RoomReaderWriter;
import model.room.Room;
import model.room.RoomStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.sort.RoomsSortCriterion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

@NeedInjectionClass
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LogManager.getLogger(RoomServiceImpl.class);
    @NeedInjectionField
    private RoomDao roomDao;
    @NeedInjectionField
    private DaoManager daoManager;
    @ConfigProperty(propertyName = "rooms.changeStatus", type = Boolean.class)
    private Boolean isChangeableStatus;

    @Override
    public void addRoom(final RoomStatus status, final BigDecimal price, final Integer capacity, final Integer stars) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            roomDao.add(new Room(status, price, capacity, stars, true), connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public void setRoomStatus(final Integer roomId, final RoomStatus status) {
        if (!isChangeableStatus) {
            LOGGER.error("Error at setting RoomStatus: No permission to change status");
            throw new BusinessException("No permission to change status");
        }

        Connection connection = daoManager.getConnection();
        Room room = roomDao.getById(roomId, connection);
        if (room == null) {
            LOGGER.error("Error at setting RoomStatus: No such room");
            throw new BusinessException("No such room");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            roomDao.updateById(room.getId(), status.toString(), "status", connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public void setRoomPrice(final Integer roomId, final BigDecimal price) {
        Connection connection = daoManager.getConnection();
        Room room = roomDao.getById(roomId, connection);
        if (room == null) {
            LOGGER.error("Error at setting room price: No such room");
            throw new BusinessException("No such room");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            roomDao.updateById(room.getId(), price, "price", connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public List<Room> getSortedRooms(final RoomsSortCriterion criterion) {
        return roomDao.getSortedRooms(criterion, daoManager.getConnection());
    }

    @Override
    public List<Room> getFreeRooms() {
        return roomDao.getFreeRooms(daoManager.getConnection());
    }

    @Override
    public List<Room> getSortedFreeRooms(final RoomsSortCriterion criterion) {
        return roomDao.getSortedFreeRooms(criterion, daoManager.getConnection());
    }

    @Override
    public BigDecimal getPriceRoom(final Integer roomId) {
        Room room = roomDao.getById(roomId, daoManager.getConnection());
        if (room == null) {
            LOGGER.error("Error at getting room price: No such room");
            throw new BusinessException("No such room");
        }

        return room.getPrice();
    }

    @Override
    public Room getRoom(final Integer roomId) {
        return roomDao.getById(roomId, daoManager.getConnection());
    }

    @Override
    public Integer getNumberOfFreeRooms() {
        return roomDao.getNumberOfFreeRooms(daoManager.getConnection());
    }

    @Override
    public void exportRooms() {
        RoomReaderWriter.writeRooms(roomDao.getAll(daoManager.getConnection()));
    }

    @Override
    public void importRooms() {
        List<Room> rooms = RoomReaderWriter.readRooms();
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            rooms.forEach(room -> {
                Room existing = roomDao.getById(room.getId(), connection);
                if (existing == null) {
                    room.setFree(true);
                    roomDao.add(room, connection);
                }
            });
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }
}

