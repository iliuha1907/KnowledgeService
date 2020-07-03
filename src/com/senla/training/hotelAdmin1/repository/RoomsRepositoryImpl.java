package com.senla.training.hotelAdmin.repository;

import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.model.room.RoomStatus;
import com.senla.training.hotelAdmin.util.RoomIdProvider;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
    public void addRoom(Room room) {
        room.setId(RoomIdProvider.getNextId());
        rooms.add(room);
    }

    @Override
    public List<Room> getFreeRoomsAfterDate(Date date) {
        List<Room> freeRooms = new LinkedList<>();
        rooms.forEach(e -> {
            if (e.getResident() == null
                    || e.getResident().getDepartureDate().compareTo(date) < 0) {
                freeRooms.add(e);
            }
        });
        return freeRooms;
    }

    @Override
    public List<Room> getFreeRooms() {
        List<Room> freeRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getResident() == null && room.getStatus().equals(RoomStatus.SERVED)) {
                freeRooms.add(room);
            }
        }
        return freeRooms;
    }

    @Override
    public Room getRoom(Integer roomId) {
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public Room getFirstFreeRoom() {
        for (Room room : rooms) {
            if (room.getResident() == null && room.getStatus().equals(RoomStatus.SERVED)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public Room getClientRoom(Integer clientId) {
        for (Room room : rooms) {
            if (room.getResident() != null && room.getResident().getId().equals(clientId)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }
}

