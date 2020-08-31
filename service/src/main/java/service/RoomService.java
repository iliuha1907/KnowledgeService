package service;

import model.room.Room;
import model.room.RoomStatus;
import util.sort.RoomsSortCriterion;

import java.math.BigDecimal;
import java.util.List;

public interface RoomService {

    void addRoom(RoomStatus status, BigDecimal price, Integer capacity,
                 Integer stars);

    void setRoomStatus(Integer roomId, RoomStatus status);

    void setRoomPrice(Integer roomId, BigDecimal price);

    List<Room> getSortedRooms(RoomsSortCriterion criterion);

    List<Room> getFreeRooms();

    List<Room> getSortedFreeRooms(RoomsSortCriterion criterion);

    BigDecimal getPriceRoom(Integer roomId);

    Room getRoom(Integer roomId);

    Integer getNumberOfFreeRooms();

    void exportRooms();

    void importRooms();
}

