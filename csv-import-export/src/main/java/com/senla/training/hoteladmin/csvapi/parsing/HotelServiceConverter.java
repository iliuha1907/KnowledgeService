package com.senla.training.hoteladmin.csvapi.parsing;

import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceField;

import java.math.BigDecimal;

public class HotelServiceConverter {

    public static String getStringFromService(final HotelService hotelService, final String separator) {
        return hotelService.getId() + separator + hotelService.getPrice() + separator + hotelService.getType()
                + separator;
    }

    public static HotelService parseService(final String data, final String separator) {
        String[] fields = data.split(separator);
        BigDecimal price = new BigDecimal(fields[HotelServiceField.PRICE.ordinal()]);
        HotelServiceType type = HotelServiceType.valueOf(fields[HotelServiceField.TYPE.ordinal()]);
        return new HotelService(price, type);
    }
}

