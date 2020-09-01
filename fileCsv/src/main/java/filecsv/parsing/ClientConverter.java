package filecsv.parsing;

import model.client.Client;
import model.client.ClientField;

public class ClientConverter {

    public static String getStringFromClient(final Client client, final String separator) {
        if (client == null) {
            return "-" + separator;
        }
        return client.getId() + separator + client.getFirstName() + separator + client.getLastName() + separator;
    }

    public static Client parseClient(final String data, final String separator) {
        String[] fields = data.split(separator);
        Integer clientId = Integer.parseInt(fields[ClientField.ID.ordinal()]);
        String clientFirstName = fields[ClientField.FIRST_NAME.ordinal()];
        String clientLastName = fields[ClientField.LAST_NAME.ordinal()];

        return new Client(clientId, clientFirstName, clientLastName);
    }
}
