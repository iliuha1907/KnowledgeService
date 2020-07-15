package com.senla.training.hoteladmin.controller;

import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import com.senla.training.hoteladmin.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class RoomController {
    @NeedInjectionField
    private RoomService roomService;

    public RoomController() {
    }

    public String addRoom(RoomStatus status, BigDecimal price, Integer capacity,
                          Integer stars) {
        roomService.addRoom(status, price, capacity, stars);
        return "Successfully added room";
    }

    public String setRoomStatus(int roomNumber, RoomStatus status) {
        try {
            roomService.setRoomStatus(roomNumber, status);
            return "Successfully modified status";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String setRoomPrice(int roomNumber, BigDecimal price) {
        try {
            roomService.setRoomPrice(roomNumber, price);
            return "Successfully modified price";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String getSortedRooms(RoomsSortCriterion criterion) {
        List<Room> rooms = roomService.getSortedRooms(criterion);
        String title = "Rooms sorted by " + criterion.toString() + "\n";
        StringBuilder result = new StringBuilder(title);
        rooms.forEach(room -> {
            result.append(room).append("\n");
        });
        return result.toString();
    }

    public String getSortedFreeRooms(RoomsSortCriterion criterion) {
        List<Room> rooms = roomService.getSortedFreeRooms(criterion);
        String title = "Free rooms sorted by " + criterion.toString() + "\n";
        StringBuilder result = new StringBuilder(title);
        rooms.forEach(room -> {
            result.append(room).append("\n");
        });
        return result.toString();
    }

    public String getFreeRoomsAfterDate(Date date) {
        List<Room> rooms = roomService.getFreeRoomsAfterDate(date);
        String title = "Rooms, free after " + DateUtil.getString(date) + "\n";
        StringBuilder result = new StringBuilder(title);
        rooms.forEach(room -> {
            result.append(room).append("\n");
        });
        return result.toString();
    }

    public String getRoomInfo(int roomNumber) {
        Room room = roomService.getRoom(roomNumber);
        if (room == null) {
            return "Error at getting info of the room: no such room";
        } else {
            return room.toString();
        }
    }

    public String getNumberOfFreeRooms() {
        return "Number of free rooms: " + roomService.getNumberOfFreeRooms();
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

    public String deserializeRoomsClients() {
        roomService.deserializeRooms();
        return "Successful deserialization of rooms";
    }

    public String serializeRoomsClients() {
        roomService.serializeRooms();
        return "Successful serialization of rooms";
    }

    public String deserializeRoomsId() {
        roomService.deserializeId();
        return "Successful deserialization of rooms idspread";
    }

    public String serializeRoomsId() {
        roomService.serializeId();
        return "Successful serialization of rooms idspread";
    }
}

