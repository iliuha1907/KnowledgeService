package com.senla.training.hoteladmin.service;

import com.senla.training.injection.annotation.InterfaceOfInjection;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@InterfaceOfInjection
public interface RoomService {

    void setRooms(List<Room> rooms);

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

    void importRooms();

    void updateRoom(Room rooms);

    void deserializeRooms();

    void serializeRooms();

    void serializeId();

    void deserializeId();

}

