package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.filecsv.parsing.ClientConverter;
import com.senla.training.hoteladmin.util.fileproperties.PropertyDataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientReaderWriter {
    private static final String FILE_NAME = PropertyDataProvider.getClientsCsv();
    private static final String SEPARATOR = PropertyDataProvider.getSeparator();

    public static void writeClients(List<Client> clients) {
        try (FileWriter fileWriter = new FileWriter(new File(FILE_NAME))) {
            for (Client client : clients) {
                fileWriter.write(ClientConverter.getStringFromResident(client, SEPARATOR) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<Client> readClients() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)))) {
            List<Client> clients = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                clients.add(ClientConverter.parseClient(line, SEPARATOR));
            }
            return clients;
        } catch (Exception ex) {
            throw new BusinessException("Could not read clients");
        }
    }
}

