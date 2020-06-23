package com.senla.training.hoteladmin.util.file;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.DateUtil;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RoomParser {

    public static String getStringFromRoom(Room room, String SEPARATOR) {
        if (room == null) {
            return "-" + SEPARATOR;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(room.getId());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getStatus());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getCapacity());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getStars());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getPrice());
        stringBuilder.append(SEPARATOR);
        return stringBuilder.toString();
    }

    public static Room parseRoom(String data, String SEPARATOR) {
        int startReading = 0;
        String[] fields = data.split(SEPARATOR);
        Integer id = Integer.parseInt(fields[startReading++]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[startReading++]);
        Integer capacity = Integer.parseInt(fields[startReading++]);
        Integer stars = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);

        if (fields[startReading].equals("-")) {
            return new Room(id, roomStatus, price, capacity, stars);
        }

        Integer clientId = Integer.parseInt(fields[startReading++]);
        String clientFirstName = fields[startReading++];
        String clientLastName = fields[startReading++];
        Date clientArrival = DateUtil.getDate(fields[startReading++]);
        Date clientDep = DateUtil.getDate(fields[startReading++]);
        Room room = new Room(id, roomStatus, price, capacity, stars);
        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        room.setResident(client);
        client.setRoom(room);
        return room;
    }

}

