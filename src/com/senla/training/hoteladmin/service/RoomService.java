package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.RoomsSortCriterion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RoomService {
    boolean addRoom(Room room);

    boolean setRoomStatus(Integer roomNumber, RoomStatus status);

    boolean setRoomPrice(Integer roomNumber, BigDecimal price);

    List<Room> getSortedRooms(RoomsSortCriterion criterion);

    List<Room> getSortedFreeRooms(RoomsSortCriterion criterion);

    List<Room> getFreeRoomsAfterDate(Date date);

    BigDecimal getPriceRoom(Integer roomNumber);

    Room getRoom(Integer roomNumber);

    Integer getNumberOfFreeRooms();

}

