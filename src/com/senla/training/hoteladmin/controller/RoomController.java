package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.service.RoomService;

import java.math.BigDecimal;
import java.util.List;

@NeedInjectionClass
public class RoomController {
    @NeedInjectionField
    private RoomService roomService;

    public String addRoom(RoomStatus status, BigDecimal price, Integer capacity, Integer stars) {
        try {
            roomService.addRoom(status, price, capacity, stars);
            return "Successfully added room";
        } catch (Exception ex) {
            return "Error at adding room: " + ex.getMessage();
        }
    }

    public String setRoomStatus(Integer roomId, RoomStatus status) {
        try {
            roomService.setRoomStatus(roomId, status);
            return "Successfully updated room";
        } catch (Exception ex) {
            return "Error at setting room status: " + ex.getMessage();
        }
    }

    public String setRoomPrice(Integer roomId, BigDecimal price) {
        try {
            roomService.setRoomPrice(roomId, price);
            return "Successfully updated room";
        } catch (Exception ex) {
            return "Error at setting room price: " + ex.getMessage();
        }
    }

    public String getSortedRooms(RoomsSortCriterion criterion) {
        List<Room> rooms;
        try {
            rooms = roomService.getSortedRooms(criterion);
        } catch (Exception ex) {
            return "Error at getting rooms: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Rooms:\n");
        rooms.forEach(room ->
                result.append(room).append("\n")
        );
        return result.toString();
    }

    public String getFreeRooms() {
        List<Room> rooms;
        try {
            rooms = roomService.getFreeRooms();
        } catch (Exception ex) {
            return "Error at getting rooms: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Free rooms:\n");
        rooms.forEach(room ->
                result.append(room).append("\n")
        );
        return result.toString();
    }

    public String getSortedFreeRooms(RoomsSortCriterion criterion) {
        List<Room> rooms;
        try {
            rooms = roomService.getSortedFreeRooms(criterion);
        } catch (Exception ex) {
            return "Error at getting rooms: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Free rooms:\n");
        rooms.forEach(room ->
                result.append(room).append("\n")
        );
        return result.toString();
    }

    public String getPriceRoom(Integer roomId) {
        try {
            return roomService.getPriceRoom(roomId).toString();
        } catch (Exception ex) {
            return "Error at getting room price";
        }
    }

    public String getRoom(Integer roomId) {
        Room room;
        try {
            room = roomService.getRoom(roomId);
        } catch (Exception ex) {
            return "Error at getting room";
        }

        if (room == null) {
            return "No such room";
        }
        return room.toString();
    }

    public String getNumberOfFreeRooms() {
        try {
            return roomService.getNumberOfFreeRooms().toString();
        } catch (Exception ex) {
            return "Error at getting number of free rooms";
        }
    }

    public String exportRooms() {
        try {
            roomService.exportRooms();
            return "Successfully exported rooms";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String importRooms() {
        try {
            roomService.importRooms();
            return "Successfully imported rooms";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }
}

