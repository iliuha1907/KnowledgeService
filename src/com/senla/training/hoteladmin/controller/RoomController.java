package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.service.RoomService;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.RoomsSortCriterion;
import com.senla.training.hoteladmin.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    public String addRoom(Integer number, RoomStatus status, BigDecimal price, Integer capacity,
                          Integer stars) {
        Room room = new Room(number, status, price, capacity, stars);
        if (roomService.addRoom(room)) {
            return "Successfully added room";
        } else {
            return "Error at adding room: number of the room already exists!";
        }
    }

    public String setRoomStatus(int roomNumber, RoomStatus status) {
        if (roomService.setRoomStatus(roomNumber, status)) {
            return "Successfully modified status";
        } else {
            return "Error at modifying status: no such room!";
        }
    }

    public String setRoomPrice(int roomNumber, BigDecimal price) {
        if (roomService.setRoomPrice(roomNumber, price)) {
            return "Successfully modified price";
        } else {
            return "Error at modifying price: no such room!";
        }
    }

    public String getSortedRooms(RoomsSortCriterion criterion) {
        List<Room> rooms = roomService.getSortedRooms(criterion);
        StringBuilder result = new StringBuilder("Rooms sorted by " + criterion.toString() + "\n");
        rooms.forEach(e -> {
            result.append(e + "\n");
        });
        return result.toString();
    }

    public String getSortedFreeRooms(RoomsSortCriterion criterion) {
        List<Room> rooms = roomService.getSortedFreeRooms(criterion);
        StringBuilder result = new StringBuilder("Free rooms sorted by " + criterion.toString() + "\n");
        rooms.forEach(e -> {
            result.append(e + "\n");
        });
        return result.toString();
    }

    public String getFreeRoomsAfterDate(Date date) {
        List<Room> rooms = roomService.getFreeRoomsAfterDate(date);
        StringBuilder result = new StringBuilder("Rooms, free after " + DateUtil.getStr(date) + "\n");
        rooms.forEach(e -> {
            result.append(e + "\n");
        });
        return result.toString();
    }

    public String getPriceRoom(int roomNumber) {
        BigDecimal price = roomService.getPriceRoom(roomNumber);
        if (price == null) {
            return "Error at getting price of the room: no such room";
        } else {
            return "Price: " + price.toString();
        }
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
}

