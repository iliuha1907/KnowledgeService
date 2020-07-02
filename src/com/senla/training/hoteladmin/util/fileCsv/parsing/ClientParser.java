package com.senla.training.hotelAdmin.util.fileCsv.parsing;

import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.util.DateUtil;

import java.util.Date;

public class ClientParser {

    public static String getStringFromResident(Client client, String separator) {
        if (client == null) {
            return "-" + separator;
        }
        String result = "";
        result += client.getId();
        result += separator;
        result += client.getFirstName();
        result += separator;
        result += client.getLastName();
        result += separator;
        result += DateUtil.getString(client.getArrivalDate());
        result += separator;
        result += DateUtil.getString(client.getDepartureDate());
        result += separator;
        return result;
    }

    //оставил все же без литералов, так как если измениться структура файла или еще что-то,
    //то придется вручную их везде менять, а счетчик к этому не чувствителен
    //ну и сам индекс нам пока не требуется
    public static Client parseClient(String data, String separator) {
        int startReading = 0;
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[startReading++]);
        String clientFirstName = fields[startReading++];
        String clientLastName = fields[startReading++];
        Date clientArrival = DateUtil.getDate(fields[startReading++]);
        Date clientDep = DateUtil.getDate(fields[startReading++]);

        Integer roomId = Integer.parseInt(fields[startReading++]);

        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        Room room = new Room();
        room.setId(roomId);
        room.setResident(client);
        client.setRoom(room);
        return client;
    }
}

