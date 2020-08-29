package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.database.RoomFields;

import java.math.BigDecimal;

public class RoomConverter {

    public static String getStringFromRoom(Room room, String separator) {
        if (room == null) {
            return "-" + separator;
        }
        return room.getId() + separator + room.getStatus() + separator + room.getCapacity() + separator
                + room.getStars() + separator + room.getPrice() + separator;
    }

    public static Room parseRoom(String data, String separator) {
        String[] fields = data.split(separator);
        Integer id = Integer.parseInt(fields[RoomFields.id.ordinal()]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[RoomFields.status.ordinal()]);
        Integer capacity = Integer.parseInt(fields[RoomFields.capacity.ordinal()]);
        Integer stars = Integer.parseInt(fields[RoomFields.stars.ordinal()]);
        BigDecimal price = new BigDecimal(fields[RoomFields.price.ordinal()]);

        return new Room(id, roomStatus, price, capacity, stars, true);
    }
}

