package com.senla.training.hotelAdmin.service;

import com.senla.training.hotelAdmin.exception.BusinessException;
import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.repository.*;
import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.model.room.RoomStatus;
import com.senla.training.hotelAdmin.util.fileCsv.writeRead.RoomWriter;
import com.senla.training.hotelAdmin.util.sort.RoomsSortCriterion;
import com.senla.training.hotelAdmin.util.sort.RoomsSorter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RoomServiceImpl implements RoomService {
    private static RoomService instance;
    private RoomsRepository roomsRepository;
    private ClientService clientService;

    private RoomServiceImpl() {
        this.roomsRepository = RoomsRepositoryImpl.getInstance();
        this.clientService = ClientServiceImpl.getInstance();
    }

    public static RoomService getInstance() {
        if (instance == null) {
            instance = new RoomServiceImpl();
            return instance;
        }
        return instance;
    }

    @Override
    public void addRoom(RoomStatus status, BigDecimal price, Integer capacity, Integer stars) {
        Room room = new Room(null, status, price, capacity, stars);
        roomsRepository.addRoom(room);
    }

    @Override
    public void setRoomStatus(Integer roomId, RoomStatus status) {
        Room room = roomsRepository.getRoom(roomId);
        if (room == null) {
            throw new BusinessException("Error at modifying room: no such room!");
        }
        room.setStatus(status);
    }

    @Override
    public void setRoomPrice(Integer roomId, BigDecimal price) {
        Room room = roomsRepository.getRoom(roomId);
        if (room == null) {
            throw new BusinessException("Error at modifying room: no such room!");
        }
        room.setPrice(price);
    }

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        List<Room> rooms = roomsRepository.getRooms();
        if (criterion.equals(RoomsSortCriterion.PRICE)) {
            RoomsSorter.sortRoomsByPrice(rooms);
        } else if (criterion.equals(RoomsSortCriterion.STARS)) {
            RoomsSorter.sortRoomsByStars(rooms);
        } else if (criterion.equals(RoomsSortCriterion.CAPACITY)) {
            RoomsSorter.sortRoomsByCapacity(rooms);
        }
        return rooms;
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        List<Room> free = getFreeRooms();
        if (criterion.equals(RoomsSortCriterion.PRICE)) {
            RoomsSorter.sortRoomsByPrice(free);
        } else if (criterion.equals(RoomsSortCriterion.STARS)) {
            RoomsSorter.sortRoomsByStars(free);
        } else if (criterion.equals(RoomsSortCriterion.CAPACITY)) {
            RoomsSorter.sortRoomsByCapacity(free);
        }
        return free;
    }

    @Override
    public List<Room> getFreeRoomsAfterDate(Date date) {
        return roomsRepository.getFreeRoomsAfterDate(date);
    }

    @Override
    public BigDecimal getPriceRoom(Integer roomId) {
        Room room = roomsRepository.getRoom(roomId);
        if (room == null) {
            throw new BusinessException("Error at modifying room: no such room!");
        }
        return room.getPrice();
    }

    @Override
    public Room getRoom(Integer roomId) {
        return roomsRepository.getRoom(roomId);
    }

    @Override
    public Integer getNumberOfFreeRooms() {
        return roomsRepository.getFreeRooms().size();
    }

    @Override
    public void exportRooms() {
        RoomWriter.writeRooms(roomsRepository.getRooms());

    }

    @Override
    public void importRooms(ClientService clientService) {
        List<Room> rooms = RoomWriter.readRooms();
        if (rooms == null) {
            throw new BusinessException("Could not import rooms!");
        }
        rooms.forEach(room -> {
            if (room.getResident() == null) {
                updateRoom(room);
            } else {
                Client existing = clientService.getClientById(room.getResident().getId());
                if (existing == null) {
                    throw new BusinessException("Could not import rooms: wrong id of a client!");
                }
                existing.getRoom().setResident(null);
                room.setResident(existing);
                existing.setRoom(room);
                updateRoom(room);
            }
        });
    }

    @Override
    public void updateRoom(Room room) {
        if (room == null) {
            return;
        }
        List<Room> rooms = roomsRepository.getRooms();
        int index = rooms.indexOf(room);
        if (index == -1) {
            roomsRepository.addRoom(room);
        } else {
            if (rooms.get(index).getResident() != null) {
                if (!rooms.get(index).getResident().equals(room.getResident())) {
                    clientService.removeResident(rooms.get(index).getResident());
                }
            }
            rooms.set(index, room);
        }
    }

    private List<Room> getFreeRooms() {
        return roomsRepository.getFreeRooms();
    }
}

