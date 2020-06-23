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

public class ClientParser {

    public static String getStringFromResident(Client client, String SEPARATOR) {
        if (client == null) {
            return "-" + SEPARATOR;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(client.getId());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(client.getFirstName());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(client.getLastName());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(DateUtil.getStr(client.getArrivalDate()));
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(DateUtil.getStr(client.getDepartureDate()));
        stringBuilder.append(SEPARATOR);
        return stringBuilder.toString();
    }

    public static Client parseClient(String data, String SEPARATOR) {
        int startReading = 0;
        String[] fields = data.split(SEPARATOR);
        Integer clientId = Integer.parseInt(fields[startReading++]);
        String clientFirstName = fields[startReading++];
        String clientLastName = fields[startReading++];
        Date clientArrival = DateUtil.getDate(fields[startReading++]);
        Date clientDep = DateUtil.getDate(fields[startReading++]);

        Integer id = Integer.parseInt(fields[startReading++]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[startReading++]);
        Integer capacity = Integer.parseInt(fields[startReading++]);
        Integer stars = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);

        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        Room room = new Room(id, roomStatus, price, capacity, stars);
        room.setResident(client);
        client.setRoom(room);
        return client;
    }
}

