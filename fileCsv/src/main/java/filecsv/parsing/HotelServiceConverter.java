package filecsv.parsing;

import model.hotelservice.HotelService;
import model.hotelservice.HotelServiceType;
import util.database.HotelServiceField;

import java.math.BigDecimal;

public class HotelServiceConverter {

    public static String getStringFromService(final HotelService hotelService, final String separator) {
        return hotelService.getId() + separator + hotelService.getPrice() + separator + hotelService.getType()
                + separator;
    }

    public static HotelService parseService(final String data, final String separator) {
        String[] fields = data.split(separator);
        Integer id = Integer.parseInt(fields[HotelServiceField.ID.ordinal()]);
        BigDecimal price = new BigDecimal(fields[HotelServiceField.PRICE.ordinal()]);
        HotelServiceType type = HotelServiceType.valueOf(fields[HotelServiceField.TYPE.ordinal()]);
        return new HotelService(id, price, type);
    }
}

