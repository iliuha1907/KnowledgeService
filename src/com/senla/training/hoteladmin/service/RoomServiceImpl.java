package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.dao.DaoManager;
import com.senla.training.hoteladmin.dao.roomdao.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

@NeedInjectionClass
public class RoomServiceImpl implements RoomService {
    @NeedInjectionField
    private RoomDao roomDao;
    @NeedInjectionField
    private DaoManager daoManager;
    @ConfigProperty(propertyName = "rooms.changeStatus", type = Boolean.class)
    private Boolean isChangeableStatus;

    @Override
    public void addRoom(RoomStatus status, BigDecimal price, Integer capacity, Integer stars) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            roomDao.add(new Room(status, price, capacity, stars, true), connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            daoManager.setConnectionAutocommit(isAutocommit);
            throw ex;
        }
        daoManager.setConnectionAutocommit(isAutocommit);
    }

    @Override
    public void setRoomStatus(Integer roomId, RoomStatus status) {
        if (!isChangeableStatus) {
            throw new BusinessException("No permission to change status");
        }

        Connection connection = daoManager.getConnection();
        Room room = roomDao.getById(roomId, connection);
        if (room == null) {
            throw new BusinessException("No such room");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            roomDao.updateById(room.getId(), status.toString(), "status", connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            daoManager.setConnectionAutocommit(isAutocommit);
            throw ex;
        }
        daoManager.setConnectionAutocommit(isAutocommit);
    }

    @Override
    public void setRoomPrice(Integer roomId, BigDecimal price) {
        Connection connection = daoManager.getConnection();
        Room room = roomDao.getById(roomId, connection);
        if (room == null) {
            throw new BusinessException("No such room");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            roomDao.updateById(room.getId(), price, "price", connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            daoManager.setConnectionAutocommit(isAutocommit);
            throw ex;
        }
        daoManager.setConnectionAutocommit(isAutocommit);
    }

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedRooms(criterion, daoManager.getConnection());
    }

    @Override
    public List<Room> getFreeRooms() {
        return roomDao.getFreeRooms(daoManager.getConnection());
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedFreeRooms(criterion, daoManager.getConnection());
    }

    @Override
    public BigDecimal getPriceRoom(Integer roomId) {
        Room room = roomDao.getById(roomId, daoManager.getConnection());
        if (room == null) {
            throw new BusinessException("No such room");
        }

        return room.getPrice();
    }

    @Override
    public Room getRoom(Integer roomId) {
        return roomDao.getById(roomId, daoManager.getConnection());
    }

    @Override
    public Integer getNumberOfFreeRooms() {
        return roomDao.getNumberOfFreeRooms(daoManager.getConnection());
    }
}

