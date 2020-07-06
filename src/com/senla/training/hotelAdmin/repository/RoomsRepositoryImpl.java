package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.id.RoomIdProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RoomsRepositoryImpl implements RoomsRepository {
    private static RoomsRepository instance;
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
    public void addRoom(Room room) {
        room.setId(RoomIdProvider.getNextId());
        rooms.add(room);
    }

    @Override
    public List<Room> getFreeRoomsAfterDate(Date date) {
        return rooms.stream().filter(room -> room.getResident() == null
                || room.getResident().getDepartureDate().before(date)).collect(Collectors.toList());
    }

    @Override
    public List<Room> getFreeRooms() {
        return rooms.stream().filter(room -> room.getResident() == null
                || room.getStatus().equals(RoomStatus.SERVED)).collect(Collectors.toList());
    }

    @Override
    public Room getRoom(Integer roomId) {
        try {
            return rooms.stream().filter(room -> room.getId().equals(roomId)).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Room getFirstFreeRoom() {
        try {
            return rooms.stream().filter(room -> room.getResident() == null && room.getStatus().
                    equals(RoomStatus.SERVED)).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public Room getClientRoom(Integer clientId) {
        try {
            return rooms.stream().filter(room -> room.getResident() != null && room.getResident().getId().
                    equals(clientId)).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }
}

