package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomFields;
import com.senla.training.hoteladmin.model.room.RoomStatus;

import java.math.BigDecimal;

public class RoomConverter {

    public static String getStringFromRoom(Room room, String separator) {
        if (room == null) {
            return "-" + separator;
        }
        return room.getId() + separator + room.getStatus() + separator + room.getCapacity() + separator
                + room.getStars() + separator + room.getPrice() + separator +
                room.getResident().getId() + separator;
    }

    public static Room parseRoom(String data, String separator) {
        String[] fields = data.split(separator);
        Integer id = Integer.parseInt(fields[RoomFields.ID.ordinal()]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[RoomFields.STATUS.ordinal()]);
        Integer capacity = Integer.parseInt(fields[RoomFields.CAPACITY.ordinal()]);
        Integer stars = Integer.parseInt(fields[RoomFields.STARS.ordinal()]);
        BigDecimal price = new BigDecimal(fields[RoomFields.PRICE.ordinal()]);

        if (fields[RoomFields.CLIENT_ID.ordinal()].equals("-")) {
            return new Room(id, roomStatus, price, capacity, stars);
        }

        Integer clientId = Integer.parseInt(fields[RoomFields.CLIENT_ID.ordinal()]);
        Room room = new Room(id, roomStatus, price, capacity, stars);
        Client client = new Client();
        client.setId(clientId);
        room.setResident(client);
        client.setRoom(room);
        return room;
    }
}

