package com.senla.training.hoteladmin.util.file;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.svc.Service;
import com.senla.training.hoteladmin.model.svc.ServiceType;
import com.senla.training.hoteladmin.repo.ClientsArchiveRepoImpl;
import com.senla.training.hoteladmin.repo.ClientsRepoImpl;
import com.senla.training.hoteladmin.repo.RoomsRepoImpl;
import com.senla.training.hoteladmin.repo.SvcRepoImpl;
import com.senla.training.hoteladmin.service.*;
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
        ClientService clientService = ClientServiceImpl.
                getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepoImpl.getInstance()),
                        SvcServiceImpl.getInstance(SvcRepoImpl.getInstance(), ServiceWriterImpl.getInstance()),
                        ClientsRepoImpl.getInstance(), RoomsRepoImpl.getInstance(), ClientWriterImpl.getInstance());

        RoomService roomService = RoomServiceImpl.getInstance
                (RoomsRepoImpl.getInstance(), RoomWriterImpl.getInstance());

        int startReading = 0;
        String[] fields = data.split(SEPARATOR);
        Integer id = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);
        ServiceType type = ServiceType.valueOf(fields[startReading++]);
        Date date = DateUtil.getDate(fields[startReading++]);

        Integer clientId = Integer.parseInt(fields[startReading++]);
        Integer roomId = Integer.parseInt(fields[startReading++]);
        Client client = clientService.getClientById(clientId);
        Room room = roomService.getRoom(roomId);
        if (client == null || room == null) {
            return null;
        }
        room.setResident(client);
        client.setRoom(room);
        return new Service(id, price, type, client, date);
    }
}

