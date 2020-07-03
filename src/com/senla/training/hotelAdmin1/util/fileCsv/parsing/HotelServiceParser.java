package com.senla.training.hotelAdmin.util.fileCsv.parsing;

import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.model.hotelService.HotelService;
import com.senla.training.hotelAdmin.model.hotelService.HotelServiceType;
import com.senla.training.hotelAdmin.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class HotelServiceParser {

    public static String getStringFromService(HotelService hotelService, String separator) {
        String result = "";
        result += hotelService.getId();
        result += separator;
        result += hotelService.getPrice();
        result += separator;
        result += hotelService.getType();
        result += separator;
        result += DateUtil.getString(hotelService.getDate());
        result += separator;
        return result;
    }

    public static HotelService parseService(String data, String separator) {
        int startReading = 0;
        String[] fields = data.split(separator);
        Integer id = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);
        HotelServiceType type = HotelServiceType.valueOf(fields[startReading++]);
        Date date = DateUtil.getDate(fields[startReading++]);

        Integer clientId = Integer.parseInt(fields[startReading++]);
        Client client = new Client();
        client.setId(clientId);
        return new HotelService(id, price, type, client, date);
    }
}

