package com.senla.training.hoteladmin.util.file;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.RoomService;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.service.RoomWriterImpl;
import com.senla.training.hoteladmin.util.DateUtil;

import java.util.Date;

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
        RoomService roomService = RoomServiceImpl.getInstance
                (RoomsRepositoryImpl.getInstance(), RoomWriterImpl.getInstance());
        int startReading = 0;
        String[] fields = data.split(SEPARATOR);
        Integer clientId = Integer.parseInt(fields[startReading++]);
        String clientFirstName = fields[startReading++];
        String clientLastName = fields[startReading++];
        Date clientArrival = DateUtil.getDate(fields[startReading++]);
        Date clientDep = DateUtil.getDate(fields[startReading++]);

        Integer roomId = Integer.parseInt(fields[startReading++]);

        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        Room room = roomService.getRoom(roomId);
        if (room == null) {
            return null;
        }
        room.setResident(client);
        client.setRoom(room);
        return client;
    }
}

