package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repo.RoomsRepo;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.RoomsSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSorter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class RoomServiceImpl implements RoomService {
    private static RoomServiceImpl instance;
    private RoomsRepo roomsRepo;

    private RoomServiceImpl(RoomsRepo roomsRepo) {
        this.roomsRepo = roomsRepo;
    }

    public static RoomServiceImpl getInstance(RoomsRepo roomsRepo) {
        if (instance == null) {
            instance = new RoomServiceImpl(roomsRepo);
            return instance;
        }
        return instance;
    }

    public boolean addRoom(Room room) {
        if (!checkRoomNumber(room.getNumber())) {
            return false;
        }
        List<Room> rooms = roomsRepo.getRooms();
        rooms.add(room);
        roomsRepo.setRooms(rooms);
        return true;
    }

    public boolean setRoomStatus(Integer roomNumber, RoomStatus status) {
        List<Room> rooms = roomsRepo.getRooms();
        ListIterator iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room room = (Room) iterator.next();
            if (room.getNumber().equals(roomNumber)) {
                room.setStatus(status);
                roomsRepo.setRooms(rooms);
                return true;
            }
        }
        return false;
    }

    public boolean setRoomPrice(Integer roomNumber, BigDecimal price) {
        List<Room> rooms = roomsRepo.getRooms();
        ListIterator iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room room = (Room) iterator.next();
            if (room.getNumber().equals(roomNumber)) {
                room.setPrice(price);
                roomsRepo.setRooms(rooms);
                return true;
            }
        }
        return false;
    }

    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        List<Room> rooms = roomsRepo.getRooms();
        switch (criterion) {
            case PRICE:
                RoomsSorter.sortRoomsByPrice(rooms);
                break;
            case STARS:
                RoomsSorter.sortRoomsByStars(rooms);
                break;
            case CAPACITY:
                RoomsSorter.sortRoomsByCapacity(rooms);
                break;
        }
        return rooms;
    }

    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        List<Room> free = getFreeRooms(roomsRepo.getRooms());
        switch (criterion) {
            case PRICE:
                RoomsSorter.sortRoomsByPrice(free);
                break;
            case STARS:
                RoomsSorter.sortRoomsByStars(free);
                break;
            case CAPACITY:
                RoomsSorter.sortRoomsByCapacity(free);
                break;
        }
        return free;
    }

    public List<Room> getFreeRoomsAfterDate(Date date) {
        List<Room> rooms = roomsRepo.getRooms();
        List<Room> free = new LinkedList<>();
        rooms.forEach(e -> {
            if (e.getResident() == null
                    || e.getResident().getDepartureDate().compareTo(date) == -1) {
                free.add(e);
            }
        });
        return free;
    }

    public BigDecimal getPriceRoom(Integer roomNumber) {
        List<Room> rooms = roomsRepo.getRooms();
        BigDecimal price = null;
        for (Room room : rooms) {
            if (room.getNumber().equals(roomNumber)) {
                price = room.getPrice();
                break;
            }
        }
        return price;
    }

    public Room getRoom(Integer roomNumber) {
        List<Room> rooms = roomsRepo.getRooms();
        for (Room room : rooms) {
            if (room.getNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    public Integer getNumberOfFreeRooms() {
        Integer freeRooms = 0;
        List<Room> rooms = roomsRepo.getRooms();
        for (Room room : rooms) {
            if (room.getResident() == null) {
                freeRooms++;
            }
        }
        return freeRooms;
    }

    private List<Room> getFreeRooms(List<Room> rooms) {
        LinkedList<Room> free = new LinkedList<>();
        rooms.forEach(e -> {
            if (e.getResident() == null) {
                free.add(e);
            }
        });
        return free;
    }

    private boolean checkRoomNumber(Integer roomNumber) {
        List<Room> rooms = roomsRepo.getRooms();
        for (Room room : rooms) {
            if (room.getNumber().equals(roomNumber)) {
                return false;
            }
        }
        return true;
    }

}

