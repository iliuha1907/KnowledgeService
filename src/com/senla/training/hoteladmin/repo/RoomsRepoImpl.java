package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.room.Room;

import java.util.LinkedList;
import java.util.List;

public class RoomsRepoImpl implements RoomsRepo{
    private static RoomsRepoImpl instance;
    private List<Room> rooms;

    private RoomsRepoImpl() {
        rooms = new LinkedList<>();
    }

    public static RoomsRepoImpl getInstance() {
        if (instance == null) {
            instance = new RoomsRepoImpl();
        }
        return instance;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}

