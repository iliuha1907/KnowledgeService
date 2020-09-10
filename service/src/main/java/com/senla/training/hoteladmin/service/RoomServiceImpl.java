package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.annotationapi.ConfigProperty;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionField;
import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.writeread.RoomReaderWriter;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomField;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;

@NeedInjectionClass
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LogManager.getLogger(RoomServiceImpl.class);
    @NeedInjectionField
    private RoomDao roomDao;
    @ConfigProperty(propertyName = "rooms.changeStatus", type = Boolean.class)
    private Boolean isChangeableStatus;

    @Override
    public void addRoom(RoomStatus status, BigDecimal price, Integer capacity, Integer stars) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            roomDao.add(new Room(status, price, capacity, stars), entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public void setRoomStatus(Integer roomId, RoomStatus status) {
        if (!isChangeableStatus) {
            LOGGER.error("Error at setting RoomStatus: No permission to change status");
            throw new BusinessException("No permission to change status");
        }

        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            roomDao.updateById(roomId, status.toString().toLowerCase(), RoomField.STATUS.toString().toLowerCase(),
                    entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public void setRoomPrice(Integer roomId, BigDecimal price) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            roomDao.updateById(roomId, price, RoomField.PRICE.toString().toLowerCase(),
                    entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedRooms(criterion, EntityManagerProvider.getEntityManager());
    }

    @Override
    public List<Room> getFreeRooms() {
        return roomDao.getFreeRooms(EntityManagerProvider.getEntityManager());
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedFreeRooms(criterion, EntityManagerProvider.getEntityManager());
    }

    @Override
    public BigDecimal getPriceRoom(Integer roomId) {
        Room room = roomDao.getById(roomId, EntityManagerProvider.getEntityManager());
        return room.getPrice();
    }

    @Override
    public Room getRoom(Integer roomId) {
        return roomDao.getById(roomId, EntityManagerProvider.getEntityManager());
    }

    @Override
    public Long getNumberOfFreeRooms() {
        return roomDao.getNumberOfFreeRooms(EntityManagerProvider.getEntityManager());
    }

    @Override
    public void exportRooms() {
        RoomReaderWriter.writeRooms(roomDao.getAll(EntityManagerProvider.getEntityManager()));
    }

    @Override
    public void importRooms() {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        List<Room> rooms = RoomReaderWriter.readRooms();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            rooms.forEach(room -> {
                room.setIsFree(1);
                roomDao.add(room, entityManager);
            });
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }
}
