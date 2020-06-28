package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repository.RoomsRepository;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.RoomIdProvider;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.util.sort.RoomsSorter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class RoomServiceImpl implements RoomService {
    private static RoomServiceImpl instance;
    private RoomsRepository roomsRepository;
    private RoomWriter roomWriter;

    private RoomServiceImpl(RoomsRepository roomsRepository, RoomWriter roomWriter) {
        this.roomsRepository = roomsRepository;
        this.roomWriter = roomWriter;
    }

    public static RoomService getInstance(RoomsRepository roomsRepository, RoomWriter roomWriter) {
        if (instance == null) {
            instance = new RoomServiceImpl(roomsRepository, roomWriter);
            return instance;
        }
        return instance;
    }

    @Override
    public boolean addRoom(Room room) {
        if (!checkRoomNumber(room.getId())) {
            return false;
        }
        List<Room> rooms = roomsRepository.getRooms();
        rooms.add(room);
        roomsRepository.setRooms(rooms);
        return true;
    }

    @Override
    public boolean setRoomStatus(Integer roomId, RoomStatus status) {
        List<Room> rooms = roomsRepository.getRooms();
        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if (room.getId().equals(roomId)) {
                room.setStatus(status);
                roomsRepository.setRooms(rooms);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setRoomPrice(Integer roomId, BigDecimal price) {
        List<Room> rooms = roomsRepository.getRooms();
        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if (room.getId().equals(roomId)) {
                room.setPrice(price);
                roomsRepository.setRooms(rooms);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        List<Room> rooms = roomsRepository.getRooms();
        if(criterion.equals(RoomsSortCriterion.PRICE)){
            RoomsSorter.sortRoomsByPrice(rooms);
        }
        else if(criterion.equals(RoomsSortCriterion.STARS)){
            RoomsSorter.sortRoomsByStars(rooms);
        }
        else if(criterion.equals(RoomsSortCriterion.CAPACITY)){
            RoomsSorter.sortRoomsByCapacity(rooms);
        }
        return rooms;
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        List<Room> free = getFreeRooms(roomsRepository.getRooms());
        if(criterion.equals(RoomsSortCriterion.PRICE)){
            RoomsSorter.sortRoomsByPrice(free);
        }
        else if(criterion.equals(RoomsSortCriterion.STARS)){
            RoomsSorter.sortRoomsByStars(free);
        }
        else if(criterion.equals(RoomsSortCriterion.CAPACITY)){
            RoomsSorter.sortRoomsByCapacity(free);
        }
        return free;
    }

    @Override
    public List<Room> getFreeRoomsAfterDate(Date date) {
        List<Room> rooms = roomsRepository.getRooms();
        List<Room> free = new LinkedList<>();
        rooms.forEach(e -> {
            if (e.getResident() == null
                    || e.getResident().getDepartureDate().compareTo(date) == -1) {
                free.add(e);
            }
        });
        return free;
    }

    @Override
    public BigDecimal getPriceRoom(Integer roomId) {
        List<Room> rooms = roomsRepository.getRooms();
        BigDecimal price = null;
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                price = room.getPrice();
                break;
            }
        }
        return price;
    }

    @Override
    public Room getRoom(Integer roomId) {
        List<Room> rooms = roomsRepository.getRooms();
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public Integer getNumberOfFreeRooms() {
        Integer freeRooms = 0;
        List<Room> rooms = roomsRepository.getRooms();
        for (Room room : rooms) {
            if (room.getResident() == null) {
                freeRooms++;
            }
        }
        return freeRooms;
    }

    @Override
    public boolean exportRooms() {
        try {
            roomWriter.writeRooms(roomsRepository.getRooms());
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean importRooms(ClientService clientService) {
        List<Room> oldRooms = roomsRepository.getRooms();
        List<Room> rooms;
        try {
            rooms = roomWriter.readRooms();
            rooms.forEach(e->{
                int index = oldRooms.indexOf(e);
                if(index!=-1 && !(oldRooms.get(index).getResident().equals(e.getResident()))) {
                    clientService.removeResident(oldRooms.get(index).getResident());
                }
                updateRoom(e);
                clientService.updateClient(e.getResident());
            });
        }
        catch (Exception ex){
            return false;
        }

        return true;
    }

    @Override
    public void updateRoom(Room room) {
        if(room == null){
            return;
        }
        List<Room> rooms = roomsRepository.getRooms();
        int index = rooms.indexOf(room);
        if(index == -1){
            room.setId(RoomIdProvider.getNextId());
            rooms.add(room);
        }
        else {
            rooms.set(index,room);
        }
        roomsRepository.setRooms(rooms);
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

    private boolean checkRoomNumber(Integer roomId) {
        List<Room> rooms = roomsRepository.getRooms();
        for (Room room : rooms) {
            if (room.getId().equals(roomId)) {
                return false;
            }
        }
        return true;
    }

}

