package com.senla.training.hoteladmin.service.room;

import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.writeread.RoomReaderWriter;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LogManager.getLogger(RoomServiceImpl.class);
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomReaderWriter roomReaderWriter;
    @Value("${rooms.changeStatus:true}")
    private boolean isChangeableStatus;

    @Override
    @Transactional
    public void addRoom(RoomStatus status, BigDecimal price, Integer capacity, Integer stars) {
        roomDao.add(new Room(status, price, capacity, stars));
    }

    @Override
    @Transactional
    public void setRoomStatus(Integer roomId, RoomStatus status) {
        if (!isChangeableStatus) {
            LOGGER.error("Error at setting RoomStatus: No permission to change status");
            throw new BusinessException("No permission to change status");
        }
        Room room = roomDao.getById(roomId);
        if (room == null) {
            throw new BusinessException("Error at setting RoomStatus: no such room!");
        }
        room.setStatus(status.toString());
        roomDao.update(room);
    }

    @Override
    @Transactional
    public void setRoomPrice(Integer roomId, BigDecimal price) {
        Room room = roomDao.getById(roomId);
        if (room == null) {
            throw new BusinessException("Error at setting RoomStatus: no such room!");
        }
        room.setPrice(price);
        roomDao.update(room);
    }

    @Override
    @Transactional
    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedRooms(criterion);
    }

    @Override
    @Transactional
    public List<Room> getFreeRooms() {
        return roomDao.getFreeRooms();
    }

    @Override
    @Transactional
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedFreeRooms(criterion);
    }

    @Override
    @Transactional
    public BigDecimal getPriceRoom(Integer roomId) {
        Room room = roomDao.getById(roomId);
        return room.getPrice();
    }

    @Override
    @Transactional
    public Room getRoom(Integer roomId) {
        return roomDao.getById(roomId);
    }

    @Override
    @Transactional
    public Long getNumberOfFreeRooms() {
        return roomDao.getNumberOfFreeRooms();
    }

    @Override
    @Transactional
    public void exportRooms() {
        roomReaderWriter.writeRooms(roomDao.getAll());
    }

    @Override
    @Transactional
    public void importRooms() {
        List<Room> rooms = roomReaderWriter.readRooms();
        rooms.forEach(room -> {
            room.setIsFree(1);
            roomDao.add(room);
        });
    }
}
