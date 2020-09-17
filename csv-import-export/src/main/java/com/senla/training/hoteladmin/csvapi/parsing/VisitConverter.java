package com.senla.training.hoteladmin.csvapi.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.model.visit.VisitField;

import java.util.Date;

public class VisitConverter {

    public static String getStringFromVisit(final Visit visit, final String separator) {
        if (visit == null) {
            return "-" + separator;
        }
        return visit.getClient().getId() + separator + visit.getService().getId()
                + separator + DateUtil.getString(visit.getDate()) + separator
                + visit.getIsActive() + separator;
    }

    public static Visit parseVisit(final String data, final String separator) {
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[VisitField.CLIENT.ordinal()]);
        Integer serviceId = Integer.parseInt(fields[VisitField.SERVICE.ordinal()]);
        Date date = DateUtil.getDate(fields[VisitField.DATE.ordinal()]);
        boolean isActive = Boolean.parseBoolean(fields[VisitField.IS_ACTIVE.ordinal()]);

        Client client = new Client(clientId);
        HotelService hotelService = new HotelService(serviceId);
        return new Visit(client, hotelService, java.sql.Date.valueOf(DateUtil.getString(date)), isActive);
    }
}
