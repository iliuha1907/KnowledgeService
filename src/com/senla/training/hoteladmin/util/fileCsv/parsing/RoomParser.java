package com.senla.training.hotelAdmin.util.fileCsv.parsing;

import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.model.room.RoomStatus;

import java.math.BigDecimal;

public class RoomParser {

    public static String getStringFromRoom(Room room, String separator) {
        if (room == null) {
            return "-" + separator;
        }
        String result = "";
        result += room.getId();
        result += separator;
        result += room.getStatus();
        result += separator;
        result += room.getCapacity();
        result += separator;
        result += room.getStars();
        result += separator;
        result += room.getPrice();
        result += separator;
        return result;
    }

    public static Room parseRoom(String data, String separator) {
        int startReading = 0;
        String[] fields = data.split(separator);
        Integer id = Integer.parseInt(fields[startReading++]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[startReading++]);
        Integer capacity = Integer.parseInt(fields[startReading++]);
        Integer stars = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);

        if (fields[startReading].equals("-")) {
            return new Room(id, roomStatus, price, capacity, stars);
        }

        Integer clientId = Integer.parseInt(fields[startReading++]);
        Room room = new Room(id, roomStatus, price, capacity, stars);
        Client client = new Client();
        client.setId(clientId);
        room.setResident(client);
        client.setRoom(room);
        return room;
    }
}

