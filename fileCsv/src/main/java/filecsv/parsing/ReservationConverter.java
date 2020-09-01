package filecsv.parsing;

import model.client.Client;
import model.reservation.Reservation;
import model.room.Room;
import util.DateUtil;
import model.reservation.ReservationField;

import java.util.Date;

public class ReservationConverter {

    public static String getStringFromReservation(final Reservation reservation, final String separator) {
        if (reservation == null) {
            return "-" + separator;
        }
        return reservation.getRoom().getId() + separator + reservation.getResident().getId() + separator
                + DateUtil.getString(reservation.getArrivalDate()) + separator
                + DateUtil.getString(reservation.getDepartureDate()) + separator + reservation.isActive() + separator;
    }

    public static Reservation parseReservation(final String data, final String separator) {
        String[] fields = data.split(separator);
        Integer roomId = Integer.parseInt(fields[ReservationField.ROOM_ID.ordinal()]);
        Integer clientId = Integer.parseInt(fields[ReservationField.RESIDENT_ID.ordinal()]);
        Date arrival = DateUtil.getDate(fields[ReservationField.ARRIVAL_DATE.ordinal()]);
        Date departure = DateUtil.getDate(fields[ReservationField.DEPARTURE_DATE.ordinal()]);
        boolean isActive = Boolean.parseBoolean(fields[ReservationField.IS_ACTIVE.ordinal()]);

        Client client = new Client(clientId);
        Room room = new Room(roomId);
        return new Reservation(room, client, arrival, departure, isActive);
    }
}
