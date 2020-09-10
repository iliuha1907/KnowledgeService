package com.senla.training.hoteladmin.csvapi.parsing;

import com.senla.training.hoteladmin.model.client.ClientField;
import com.senla.training.hoteladmin.model.client.Client;

public class ClientConverter {

    public static String getStringFromClient(final Client client, final String separator) {
        if (client == null) {
            return "-" + separator;
        }
        return client.getId() + separator + client.getFirstName() + separator + client.getLastName() + separator;
    }

    public static Client parseClient(final String data, final String separator) {
        String[] fields = data.split(separator);
        String clientFirstName = fields[ClientField.FIRST_NAME.ordinal()];
        String clientLastName = fields[ClientField.LAST_NAME.ordinal()];

        return new Client(clientFirstName, clientLastName);
    }
}
