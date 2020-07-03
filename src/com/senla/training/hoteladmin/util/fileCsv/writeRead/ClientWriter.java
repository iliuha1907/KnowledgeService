package com.senla.training.hotelAdmin.util.fileCsv.writeRead;

import com.senla.training.hotelAdmin.exception.BusinessException;
import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.util.fileCsv.parsing.ClientParser;
import com.senla.training.hotelAdmin.util.fileProperties.PropertyDataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientWriter {
    private final static String FILE_NAME = PropertyDataProvider.getClientsCsv();
    private final static String SEPARATOR = PropertyDataProvider.getSeparator();

    public static void writeClients(List<Client> clients) {
        try (FileWriter fileWriter = new FileWriter(new File(FILE_NAME))) {
            for (Client client : clients) {
                fileWriter.write(ClientParser.getStringFromResident(client, SEPARATOR));
                fileWriter.write(client.getRoom().getId() + SEPARATOR + "\n");
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
                clients.add(ClientParser.parseClient(line, SEPARATOR));
            }
            return clients;
        } catch (Exception ex) {
            return null;
        }
    }
}

