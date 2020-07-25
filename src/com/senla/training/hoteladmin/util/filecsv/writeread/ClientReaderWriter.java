package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.filecsv.parsing.ClientConverter;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class ClientReaderWriter {
    @ConfigProperty(propertyName = "csv.clients.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeClients(List<Client> clients) {
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            for (Client client : clients) {
                fileWriter.write(ClientConverter.getStringFromResident(client, separator) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<Client> readClients() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            List<Client> clients = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                clients.add(ClientConverter.parseClient(line, separator));
            }
            return clients;
        } catch (Exception ex) {
            throw new BusinessException("Could not read clients");
        }
    }
}

