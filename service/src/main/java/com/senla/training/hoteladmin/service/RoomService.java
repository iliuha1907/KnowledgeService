package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;

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

    Long getNumberOfFreeRooms();

    void exportRooms();

    void importRooms();
}
