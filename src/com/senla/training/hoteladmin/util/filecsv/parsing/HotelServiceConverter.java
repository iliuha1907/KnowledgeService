package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.LiteralNumberProvider;

import java.math.BigDecimal;
import java.util.Date;

public class HotelServiceConverter {

    public static String getStringFromService(HotelService hotelService, String separator) {
        return hotelService.getId() + separator + hotelService.getPrice() + separator + hotelService.getType() +
                separator + DateUtil.getString(hotelService.getDate()) +
                separator + hotelService.getClient().getId() + separator;
    }

    public static HotelService parseService(String data, String separator) {
        String[] fields = data.split(separator);
        Integer id = Integer.parseInt(fields[LiteralNumberProvider.ZERO]);
        BigDecimal price = new BigDecimal(fields[LiteralNumberProvider.ONE]);
        HotelServiceType type = HotelServiceType.valueOf(fields[LiteralNumberProvider.TWO]);
        Date date = DateUtil.getDate(fields[LiteralNumberProvider.THREE]);

        Integer clientId = Integer.parseInt(fields[LiteralNumberProvider.FOUR]);
        Client client = new Client();
        client.setId(clientId);
        return new HotelService(id, price, type, client, date);
    }
}

