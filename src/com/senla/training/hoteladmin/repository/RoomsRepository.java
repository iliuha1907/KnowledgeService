package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.room.Room;

import java.util.List;

public interface RoomsRepository {
    void setRooms(List<Room> rooms);

    List<Room> getRooms();
}

