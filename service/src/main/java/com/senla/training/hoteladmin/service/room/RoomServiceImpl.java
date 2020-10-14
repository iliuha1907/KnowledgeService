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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
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
    public void updateRoom(Room room, Integer id) {
        if (room == null) {
            LOGGER.error("Error at updating Room: Room is null");
            throw new BusinessException("Error at updating Room: Room is null");
        }

        if (!isChangeableStatus) {
            LOGGER.error("Error at updating Room: No permission to change status");
            throw new BusinessException("Error at updating Room: No permission to change status");
        }

        room.setId(id);
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
        Room room = roomDao.getByPrimaryKey(roomId);
        if (room == null) {
            throw new BusinessException("Error at getting Room price: no such room!");
        }
        return room.getPrice();
    }

    @Override
    @Transactional
    public Room getRoom(Integer roomId) {
        Room room = roomDao.getByPrimaryKey(roomId);
        if (room == null) {
            throw new BusinessException("Error at getting Room: no such entity!");
        }
        return roomDao.getByPrimaryKey(roomId);
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

    public void setChangeableStatus(boolean changeableStatus) {
        isChangeableStatus = changeableStatus;
    }
}
