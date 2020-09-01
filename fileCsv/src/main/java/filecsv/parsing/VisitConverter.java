package filecsv.parsing;

import model.client.Client;
import model.hotelservice.HotelService;
import model.visit.Visit;
import util.DateUtil;
import model.visit.VisitField;

import java.util.Date;

public class VisitConverter {

    public static String getStringFromVisit(final Visit visit, final String separator) {
        if (visit == null) {
            return "-" + separator;
        }
        return visit.getClient().getId() + separator + visit.getHotelService().getId() + separator
                + DateUtil.getString(visit.getDate()) + separator
                + visit.isActive() + separator;
    }

    public static Visit parseVisit(final String data, final String separator) {
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[VisitField.CLIENT_ID.ordinal()]);
        Integer serviceId = Integer.parseInt(fields[VisitField.HOTEL_SERVICE_ID.ordinal()]);
        Date date = DateUtil.getDate(fields[VisitField.DATE.ordinal()]);
        boolean isActive = Boolean.parseBoolean(fields[VisitField.IS_ACTIVE.ordinal()]);

        Client client = new Client(clientId);
        HotelService hotelService = new HotelService(serviceId);
        return new Visit(client, hotelService, date, isActive);
    }
}
