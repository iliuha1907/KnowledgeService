package com.senla.training.hoteladmin.util.file;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.svc.Service;
import com.senla.training.hoteladmin.model.svc.ServiceType;
import com.senla.training.hoteladmin.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class ServiceParser {

    public static String getStringFromService(Service service, String SEPARATOR) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(service.getId());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(service.getPrice());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(service.getType());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(DateUtil.getStr(service.getDate()));
        stringBuilder.append(SEPARATOR);
        return stringBuilder.toString();
    }

    public static Service parseService(String data, String SEPARATOR) {
        int startReading = 0;
        String[] fields = data.split(SEPARATOR);
        Integer id = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);
        ServiceType type = ServiceType.valueOf(fields[startReading++]);
        Date date = DateUtil.getDate(fields[startReading++]);

        Integer clientId = Integer.parseInt(fields[startReading++]);
        String clientFirstName = fields[startReading++];
        String clientLastName = fields[startReading++];
        Date clientArrival = DateUtil.getDate(fields[startReading++]);
        Date clientDep = DateUtil.getDate(fields[startReading++]);

        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        Room room;
        Integer roomId = Integer.parseInt(fields[startReading++]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[startReading++]);
        Integer capacity = Integer.parseInt(fields[startReading++]);
        Integer stars = Integer.parseInt(fields[startReading++]);
        BigDecimal roomPrice = new BigDecimal(fields[startReading++]);
        room = new Room(roomId, roomStatus, roomPrice, capacity, stars);
        room.setResident(client);
        client.setRoom(room);
        return new Service(id, price, type, client, date);
    }
}

