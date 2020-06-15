package com.senla.training.hoteladmin.util.sort;

import com.senla.training.hoteladmin.model.room.Room;

import java.util.List;

public class RoomsSorter {
    public static void sortRoomsByPrice(List<Room> rooms) {
        rooms.sort((Room o1, Room o2) -> {
            if (o1.getPrice() == null && o2.getPrice() == null) {
                return 0;
            }
            if (o1.getPrice() == null && o2.getPrice() != null) {
                return -1;
            }
            if (o1.getPrice() != null && o2.getPrice() == null) {
                return 1;
            }
            return o1.getPrice().compareTo(o2.getPrice());
        });
    }

    public static void sortRoomsByStars(List<Room> rooms) {
        rooms.sort((Room o1, Room o2) -> {
            if (o1.getStars() == null && o2.getStars() == null) {
                return 0;
            }
            if (o1.getStars() == null && o2.getStars() != null) {
                return -1;
            }
            if (o1.getStars() != null && o2.getStars() == null) {
                return 1;
            }
            return o1.getStars().compareTo(o2.getStars());
        });
    }

    public static void sortRoomsByCapacity(List<Room> rooms) {
        rooms.sort((Room o1, Room o2) -> {
            if (o1.getCapacity() == null && o2.getCapacity() == null) {
                return 0;
            }
            if (o1.getCapacity() == null && o2.getCapacity() != null) {
                return -1;
            }
            if (o1.getCapacity() != null && o2.getCapacity() == null) {
                return 1;
            }
            return o1.getCapacity().compareTo(o2.getCapacity());
        });
    }
}

