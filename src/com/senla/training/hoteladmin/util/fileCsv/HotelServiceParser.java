package com.senla.training.hoteladmin.util.fileCsv;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.svc.HotelService;
import com.senla.training.hoteladmin.model.svc.HotelServiceType;
import com.senla.training.hoteladmin.repository.ClientsArchiveRepositoryImpl;
import com.senla.training.hoteladmin.repository.ClientsRepositoryImpl;
import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.repository.HotelServiceRepositoryImpl;
import com.senla.training.hoteladmin.service.*;
import com.senla.training.hoteladmin.service.writer.ClientWriterImpl;
import com.senla.training.hoteladmin.service.writer.HotelServiceWriterImpl;
import com.senla.training.hoteladmin.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class HotelServiceParser {

    public static String getStringFromService(HotelService hotelService, String SEPARATOR) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hotelService.getId());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(hotelService.getPrice());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(hotelService.getType());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(DateUtil.getStr(hotelService.getDate()));
        stringBuilder.append(SEPARATOR);
        return stringBuilder.toString();
    }

    public static HotelService parseService(String data, String SEPARATOR) {
        ClientService clientService = ClientServiceImpl.
                getInstance(ArchivServiceImpl.getInstance(ClientsArchiveRepositoryImpl.getInstance()),
                        HotelServiceServiceImpl.getInstance(HotelServiceRepositoryImpl.getInstance(), HotelServiceWriterImpl.getInstance()),
                        ClientsRepositoryImpl.getInstance(), RoomsRepositoryImpl.getInstance(), ClientWriterImpl.getInstance());

        int startReading = 0;
        String[] fields = data.split(SEPARATOR);
        Integer id = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);
        HotelServiceType type = HotelServiceType.valueOf(fields[startReading++]);
        Date date = DateUtil.getDate(fields[startReading++]);

        Integer clientId = Integer.parseInt(fields[startReading++]);
        Client client = clientService.getClientById(clientId);

        if (client == null) {
            return null;
        }
        return new HotelService(id, price, type, client, date);
    }
}

