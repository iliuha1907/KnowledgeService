package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.client.ClientFields;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.DateUtil;

import java.util.Date;

public class ClientConverter {

    public static String getStringFromResident(Client client, String separator) {
        if (client == null) {
            return "-" + separator;
        }
        return client.getId() + separator + client.getFirstName() + separator + client.getLastName() + separator +
                DateUtil.getString(client.getArrivalDate()) + separator + DateUtil.getString(client.getDepartureDate()) +
                separator + client.getRoom().getId() + separator;

    }

    public static Client parseClient(String data, String separator) {
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[ClientFields.ID.ordinal()]);
        String clientFirstName = fields[ClientFields.FIRST_NAME.ordinal()];
        String clientLastName = fields[ClientFields.LAST_NAME.ordinal()];
        Date clientArrival = DateUtil.getDate(fields[ClientFields.ARRIVAL.ordinal()]);
        Date clientDep = DateUtil.getDate(fields[ClientFields.DEPARTURE.ordinal()]);

        Integer roomId = Integer.parseInt(fields[ClientFields.ROOM_ID.ordinal()]);

        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        Room room = new Room();
        room.setId(roomId);
        room.setResident(client);
        client.setRoom(room);
        return client;
    }
}

