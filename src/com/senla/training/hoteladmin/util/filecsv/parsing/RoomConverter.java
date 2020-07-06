package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.*;

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
        Integer id = Integer.parseInt(fields[LiteralNumberProvider.ZERO]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[LiteralNumberProvider.ONE]);
        Integer capacity = Integer.parseInt(fields[LiteralNumberProvider.TWO]);
        Integer stars = Integer.parseInt(fields[LiteralNumberProvider.THREE]);
        BigDecimal price = new BigDecimal(fields[LiteralNumberProvider.FOUR]);

        if (fields[LiteralNumberProvider.FIVE].equals("-")) {
            return new Room(id, roomStatus, price, capacity, stars);
        }

        Integer clientId = Integer.parseInt(fields[LiteralNumberProvider.SIX]);
        Room room = new Room(id, roomStatus, price, capacity, stars);
        Client client = new Client();
        client.setId(clientId);
        room.setResident(client);
        client.setRoom(room);
        return room;
    }
}

