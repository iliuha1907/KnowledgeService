package com.senla.training.hotelAdmin.repository;

import com.senla.training.hotelAdmin.model.room.Room;

import java.util.Date;
import java.util.List;

public interface RoomsRepository {
    void setRooms(List<Room> rooms);

    void addRoom(Room room);

    List<Room> getFreeRoomsAfterDate(Date date);

    List<Room> getFreeRooms();

    Room getRoom(Integer roomId);

    Room getFirstFreeRoom();

    Room getClientRoom(Integer clientId);

    List<Room> getRooms();
}

