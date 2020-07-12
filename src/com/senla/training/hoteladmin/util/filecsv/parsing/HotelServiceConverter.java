package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceFields;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.util.DateUtil;

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
        Integer id = Integer.parseInt(fields[HotelServiceFields.ID.ordinal()]);
        BigDecimal price = new BigDecimal(fields[HotelServiceFields.PRICE.ordinal()]);
        HotelServiceType type = HotelServiceType.valueOf(fields[HotelServiceFields.TYPE.ordinal()]);
        Date date = DateUtil.getDate(fields[HotelServiceFields.DATE.ordinal()]);

        Integer clientId = Integer.parseInt(fields[HotelServiceFields.CLIENT_ID.ordinal()]);
        Client client = new Client();
        client.setId(clientId);
        return new HotelService(id, price, type, client, date);
    }
}

