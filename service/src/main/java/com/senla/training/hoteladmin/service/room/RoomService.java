package com.senla.training.hoteladmin.service.room;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional
public interface RoomService {

    void addRoom(RoomStatus status, BigDecimal price, Integer capacity,
                 Integer stars);

    void updateRoom(Room room, Integer id);

    List<Room> getSortedRooms(String criterion);

    List<Room> getFreeRooms();

    List<Room> getSortedFreeRooms(String criterion);

    BigDecimal getPriceRoom(Integer roomId);

    Room getRoom(Integer roomId);

    Long getNumberOfFreeRooms();

    void exportRooms();

    void importRooms();
}
