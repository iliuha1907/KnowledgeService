package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.room.Room;

import java.util.List;

public interface RoomsRepo {
    void setRooms(List<Room> rooms);

    List<Room> getRooms();
}

