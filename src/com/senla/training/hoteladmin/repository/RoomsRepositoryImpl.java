package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.room.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomsRepositoryImpl implements RoomsRepository {
    private static RoomsRepositoryImpl instance;
    private List<Room> rooms;

    private RoomsRepositoryImpl() {
        rooms = new ArrayList<>();
    }

    public static RoomsRepository getInstance() {
        if (instance == null) {
            instance = new RoomsRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }
}

