package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.model.room.Room;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class RoomsManipulator {

    public static void sortRoomsByPrice(Room[] rooms) {
        Arrays.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return (int) (o1.getPrice() - o2.getPrice());
            }
        });
    }

    public static void sortRoomsByStars(Room[] rooms) {
        Arrays.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return (int) (o1.getStars() - o2.getStars());
            }
        });
    }

    public static void sortRoomsByCapacity(Room[] rooms) {
        Arrays.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room o1, Room o2) {
                return (int) (o1.getCapacity() - o2.getCapacity());
            }
        });
    }

    public static double getRoomPrice(Room[] rooms, int roomNumber) {
        double price = -1;
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                break;
            }
            if (rooms[i].getNumber() == roomNumber) {
                price = rooms[i].getPrice();
                break;
            }
        }
        return price;
    }

    public static void displayFreeRoomsAfterDate(Room[] rooms, Date date) {
        System.out.println("Rooms, free after " + DateUtil.getStr(date) + ":");
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                break;
            }

            if (rooms[i].getResident() == null
                    || rooms[i].getResident().getDepartureDate().compareTo(date) == -1) {
                System.out.println(rooms[i]);
            }
        }
    }

    public static void displayRoom(Room[] rooms, int roomNumber) {
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                break;
            }
            if (rooms[i].getNumber() == roomNumber) {
                System.out.println(rooms[i]);
                break;
            }
        }
    }

    public static Room[] getRealRooms(Room[] rooms, int count) {
        Room[] realRooms = new Room[count];
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                break;
            }
            realRooms[i] = rooms[i];
        }
        return realRooms;
    }

    public static Room[] getFreeRooms(Room[] rooms, int freeRooms) {
        Room[] free = new Room[freeRooms];
        int k = 0;
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                break;
            }
            if (rooms[i].getResident() == null) {
                free[k++] = rooms[i];
            }
        }
        return free;
    }
}

