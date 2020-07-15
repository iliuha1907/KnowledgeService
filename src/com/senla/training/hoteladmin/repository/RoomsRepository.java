package com.senla.training.hoteladmin.repository;

import com.senla.training.injection.annotation.InterfaceOfInjection;
import com.senla.training.hoteladmin.model.room.Room;

import java.util.Date;
import java.util.List;

@InterfaceOfInjection
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

