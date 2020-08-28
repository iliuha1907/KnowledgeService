package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.util.database.HotelServiceFields;

import java.math.BigDecimal;

public class HotelServiceConverter {

    public static String getStringFromService(HotelService hotelService, String separator) {
        return hotelService.getId() + separator + hotelService.getPrice() + separator + hotelService.getType() +
                separator;
    }

    public static HotelService parseService(String data, String separator) {
        String[] fields = data.split(separator);
        Integer id = Integer.parseInt(fields[HotelServiceFields.id.ordinal()]);
        BigDecimal price = new BigDecimal(fields[HotelServiceFields.price.ordinal()]);
        HotelServiceType type = HotelServiceType.valueOf(fields[HotelServiceFields.type.ordinal()]);
        return new HotelService(id, price, type);
    }
}

