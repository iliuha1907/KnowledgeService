package com.senla.training.hoteladmin.service;

import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.repository.*;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.filecsv.writeread.RoomReaderWriter;
import com.senla.training.hoteladmin.util.idspread.RoomIdProvider;
import com.senla.training.hoteladmin.util.serializator.Deserializator;
import com.senla.training.hoteladmin.util.serializator.Serializator;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSorter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class RoomServiceImpl implements RoomService {
    @NeedInjectionField
    private RoomsRepository roomsRepository;
    @NeedInjectionField
    private ClientsRepository clientsRepository;
    @ConfigProperty(propertyName = "rooms.changeStatus", type = Boolean.class)
    private Boolean isChangeableStatus;

    public RoomServiceImpl() {
    }

    @Override
    public void setRooms(List<Room> rooms) {
        roomsRepository.setRooms(rooms);
    }

    @Override
    public void addRoom(RoomStatus status, BigDecimal price, Integer capacity, Integer stars) {
        Room room = new Room(status, price, capacity, stars);
        roomsRepository.addRoom(room);
    }

    @Override
    public void setRoomStatus(Integer roomId, RoomStatus status) {
        if (!isChangeableStatus) {
            throw new BusinessException("Error at modifying room: no permission!");
        }
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
        RoomReaderWriter.writeRooms(roomsRepository.getRooms());
    }

    @Override
    public void importRooms() {
        List<Room> rooms = RoomReaderWriter.readRooms();
        rooms.forEach(room -> {
            if (room.getResident() == null) {
                updateRoom(room);
            } else {
                Client existing = clientsRepository.getClientById(room.getResident().getId());
                if (existing == null) {
                    throw new BusinessException("Could not import rooms: wrong idspread of a client!");
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
                    clientsRepository.removeClient(rooms.get(index).getResident());
                }
            }
            rooms.set(index, room);
        }
    }

    @Override
    public void deserializeRooms() {
        List<Room> rooms = Deserializator.deserializeRoomClients();
        setRooms(rooms);
        clientsRepository.setClients(getClientsFromRooms(rooms));
    }

    @Override
    public void serializeRooms() {
        Serializator.serializeRoomsClients(roomsRepository.getRooms());
    }

    @Override
    public void serializeId() {
        Serializator.serializeRoomId(RoomIdProvider.getCurrentId());
    }

    @Override
    public void deserializeId() {
        Integer id = Deserializator.deserializeRoomId();
        RoomIdProvider.setCurrentId(id);
    }

    private List<Room> getFreeRooms() {
        return roomsRepository.getFreeRooms();
    }

    private List<Client> getClientsFromRooms(List<Room> rooms) {
        List<Client> clients = new ArrayList<>();
        rooms.forEach(room -> {
            if (room.getResident() != null) {
                clients.add(room.getResident());
            }
        });
        return clients;
    }
}

