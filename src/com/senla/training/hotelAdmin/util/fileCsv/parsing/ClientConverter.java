package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.DateUtil;
import com.senla.training.hoteladmin.util.LiteralNumberProvider;


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

    //оставил все же без литералов, так как если измениться структура файла или еще что-то,
    //то придется вручную их везде менять, а счетчик к этому не чувствителен
    //ну и сам индекс нам пока не требуется
    public static Client parseClient(String data, String separator) {
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[LiteralNumberProvider.ZERO]);
        String clientFirstName = fields[LiteralNumberProvider.ONE];
        String clientLastName = fields[LiteralNumberProvider.TWO];
        Date clientArrival = DateUtil.getDate(fields[LiteralNumberProvider.THREE]);
        Date clientDep = DateUtil.getDate(fields[LiteralNumberProvider.FOUR]);

        Integer roomId = Integer.parseInt(fields[LiteralNumberProvider.FIVE]);

        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        Room room = new Room();
        room.setId(roomId);
        room.setResident(client);
        client.setRoom(room);
        return client;
    }
}

