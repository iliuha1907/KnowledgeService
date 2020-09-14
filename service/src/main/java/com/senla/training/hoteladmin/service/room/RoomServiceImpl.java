package com.senla.training.hoteladmin.service.room;

import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.writeread.RoomReaderWriter;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.Room_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

@Component
@Transactional
public class RoomServiceImpl implements RoomService {

    private static final Logger LOGGER = LogManager.getLogger(RoomServiceImpl.class);
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private EntityManagerProvider entityManagerProvider;
    @Autowired
    private RoomReaderWriter roomReaderWriter;
    @Value("${rooms.changeStatus:true}")
    private boolean isChangeableStatus;

    @Override
    public void addRoom(RoomStatus status, BigDecimal price, Integer capacity, Integer stars) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        try {
            roomDao.add(new Room(status, price, capacity, stars), entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void setRoomStatus(Integer roomId, RoomStatus status) {
        if (!isChangeableStatus) {
            LOGGER.error("Error at setting RoomStatus: No permission to change status");
            throw new BusinessException("No permission to change status");
        }

        EntityManager entityManager = entityManagerProvider.getEntityManager();
        try {
            roomDao.updateByAttribute(roomId, Room_.id, status.toString(), Room_.status,
                    entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void setRoomPrice(Integer roomId, BigDecimal price) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        try {
            roomDao.updateByAttribute(roomId, Room_.id, price, Room_.price, entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedRooms(criterion, entityManagerProvider.getEntityManager());
    }

    @Override
    public List<Room> getFreeRooms() {
        return roomDao.getFreeRooms(entityManagerProvider.getEntityManager());
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        return roomDao.getSortedFreeRooms(criterion, entityManagerProvider.getEntityManager());
    }

    @Override
    public BigDecimal getPriceRoom(Integer roomId) {
        Room room = roomDao.getById(roomId, entityManagerProvider.getEntityManager());
        return room.getPrice();
    }

    @Override
    public Room getRoom(Integer roomId) {
        return roomDao.getById(roomId, entityManagerProvider.getEntityManager());
    }

    @Override
    public Long getNumberOfFreeRooms() {
        return roomDao.getNumberOfFreeRooms(entityManagerProvider.getEntityManager());
    }

    @Override
    public void exportRooms() {
        roomReaderWriter.writeRooms(roomDao.getAll(entityManagerProvider.getEntityManager()));
    }

    @Override
    public void importRooms() {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        List<Room> rooms = roomReaderWriter.readRooms();
        try {
            rooms.forEach(room -> {
                room.setIsFree(1);
                roomDao.add(room, entityManager);
            });
        } catch (Exception ex) {
            throw ex;
        }
    }
}
