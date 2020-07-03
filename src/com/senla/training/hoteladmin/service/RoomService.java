package com.senla.training.hotelAdmin.service;

import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.model.room.RoomStatus;
import com.senla.training.hotelAdmin.util.sort.RoomsSortCriterion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RoomService {
    void addRoom(RoomStatus status, BigDecimal price, Integer capacity,
                 Integer stars);

    void setRoomStatus(Integer roomNumber, RoomStatus status);

    void setRoomPrice(Integer roomNumber, BigDecimal price);

    List<Room> getSortedRooms(RoomsSortCriterion criterion);

    List<Room> getSortedFreeRooms(RoomsSortCriterion criterion);

    List<Room> getFreeRoomsAfterDate(Date date);

    BigDecimal getPriceRoom(Integer roomNumber);

    Room getRoom(Integer roomId);

    Integer getNumberOfFreeRooms();

    void exportRooms();

    void importRooms(ClientService clientService);

    void updateRoom(Room rooms);

}

