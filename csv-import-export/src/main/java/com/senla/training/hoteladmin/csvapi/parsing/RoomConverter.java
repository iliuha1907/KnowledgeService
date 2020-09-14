package com.senla.training.hoteladmin.csvapi.parsing;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.RoomField;

import java.math.BigDecimal;

public class RoomConverter {

    public static String getStringFromRoom(final Room room, final String separator) {
        if (room == null) {
            return "-" + separator;
        }
        return room.getId() + separator + room.getStatus() + separator + room.getCapacity() + separator
                + room.getStars() + separator + room.getPrice() + separator;
    }

    public static Room parseRoom(final String data, final String separator) {
        String[] fields = data.split(separator);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[RoomField.STATUS.ordinal()]);
        Integer capacity = Integer.parseInt(fields[RoomField.CAPACITY.ordinal()]);
        Integer stars = Integer.parseInt(fields[RoomField.STARS.ordinal()]);
        BigDecimal price = new BigDecimal(fields[RoomField.PRICE.ordinal()]);

        return new Room(roomStatus, price, capacity, stars, true);
    }
}

