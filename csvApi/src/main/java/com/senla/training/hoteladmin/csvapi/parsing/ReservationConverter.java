package com.senla.training.hoteladmin.csvapi.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.model.reservation.ReservationField;

import java.util.Date;

public class ReservationConverter {

    public static String getStringFromReservation(final Reservation reservation, final String separator) {
        if (reservation == null) {
            return "-" + separator;
        }
        return reservation.getRoom().getId() + separator + reservation.getResident().getId() + separator
                + DateUtil.getString(reservation.getArrivalDate()) + separator
                + DateUtil.getString(reservation.getDeparture()) + separator + reservation.getIsActive() + separator;
    }

    public static Reservation parseReservation(final String data, final String separator) {
        String[] fields = data.split(separator);
        Integer roomId = Integer.parseInt(fields[ReservationField.ROOM.ordinal()]);
        Integer clientId = Integer.parseInt(fields[ReservationField.RESIDENT.ordinal()]);
        Date arrival = DateUtil.getDate(fields[ReservationField.ARRIVAL_DATE.ordinal()]);
        Date departure = DateUtil.getDate(fields[ReservationField.DEPARTURE_DATE.ordinal()]);
        Integer isActive = Integer.parseInt(fields[ReservationField.IS_ACTIVE.ordinal()]);

        Client client = new Client(clientId);
        Room room = new Room(roomId);
        return new Reservation(room, client, java.sql.Date.valueOf(DateUtil.getString(arrival)),
                java.sql.Date.valueOf(DateUtil.getString(departure)), isActive);
    }
}
