package com.senla.training.hoteladmin.csvapi.writeread;

import com.senla.training.hoteladmin.csvapi.parsing.ClientConverter;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ClientReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(ClientReaderWriter.class);
    @Value("${csv.clients.import.filePath:csv/clients.csv}")
    private String fileNameImport;
    @Value("${csv.export.directoryPath:export-csv}")
    private String exportDirectory;
    @Value("${csv.clients.export.fileName:clients.csv}")
    private String fileNameExport;
    @Value("${csv.separator:;}")
    private String separator;

    public void writeClients(final List<Client> clients) {
        try (FileWriter fileWriter = new FileWriter(new File(exportDirectory + "/" + fileNameExport))) {
            for (Client client : clients) {
                fileWriter.write(ClientConverter.getStringFromClient(client, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing clients: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public List<Client> readClients() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                ClientReaderWriter.class.getClassLoader().getResource(fileNameImport).getFile())))) {
            List<Client> clients = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                clients.add(ClientConverter.parseClient(line, separator));
            }
            return clients;
        } catch (Exception ex) {
            LOGGER.error("Error at reading clients: " + ex.getMessage());
            throw new BusinessException("Could not read clients");
        }
    }
}


