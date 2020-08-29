package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.database.VisitFields;

import java.util.Date;

public class VisitConverter {

    public static String getStringFromVisit(Visit visit, String separator) {
        if (visit == null) {
            return "-" + separator;
        }
        return visit.getClient().getId() + separator + visit.getHotelService().getId() + separator
                + DateUtil.getString(visit.getDate()) + separator
                + visit.isActive() + separator;
    }

    public static Visit parseVisit(String data, String separator) {
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[VisitFields.client_id.ordinal()]);
        Integer serviceId = Integer.parseInt(fields[VisitFields.hotel_service_id.ordinal()]);
        Date date = DateUtil.getDate(fields[VisitFields.date.ordinal()]);
        boolean isActive = Boolean.parseBoolean(fields[VisitFields.is_active.ordinal()]);

        Client client = new Client(clientId);
        HotelService hotelService = new HotelService(serviceId);
        return new Visit(client, hotelService, date, isActive);
    }
}
