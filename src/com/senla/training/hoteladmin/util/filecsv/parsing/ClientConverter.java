package com.senla.training.hoteladmin.util.filecsv.parsing;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.database.ClientFields;

public class ClientConverter {

    public static String getStringFromClient(Client client, String separator) {
        if (client == null) {
            return "-" + separator;
        }
        return client.getId() + separator + client.getFirstName() + separator + client.getLastName() + separator;

    }

    public static Client parseClient(String data, String separator) {
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[ClientFields.id.ordinal()]);
        String clientFirstName = fields[ClientFields.first_name.ordinal()];
        String clientLastName = fields[ClientFields.last_name.ordinal()];

        return new Client(clientId, clientFirstName, clientLastName);
    }
}

