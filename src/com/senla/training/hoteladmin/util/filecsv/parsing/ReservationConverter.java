package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.database.ReservationFields;

import java.util.Date;

public class ReservationConverter {

    public static String getStringFromReservation(Reservation reservation, String separator) {
        if (reservation == null) {
            return "-" + separator;
        }
        return reservation.getRoom().getId() + separator + reservation.getResident().getId() + separator +
                DateUtil.getString(reservation.getArrivalDate()) + separator
                + DateUtil.getString(reservation.getDepartureDate()) + separator + reservation.isActive() + separator;
    }

    public static Reservation parseReservation(String data, String separator) {
        String[] fields = data.split(separator);
        Integer roomId = Integer.parseInt(fields[ReservationFields.room_id.ordinal()]);
        Integer clientId = Integer.parseInt(fields[ReservationFields.resident_id.ordinal()]);
        Date arrival = DateUtil.getDate(fields[ReservationFields.arrival_date.ordinal()]);
        Date departure = DateUtil.getDate(fields[ReservationFields.departure_date.ordinal()]);
        boolean isActive = Boolean.parseBoolean(fields[ReservationFields.is_active.ordinal()]);

        Client client = new Client(clientId);
        Room room = new Room(roomId);
        return new Reservation(room, client, arrival, departure, isActive);
    }
}
