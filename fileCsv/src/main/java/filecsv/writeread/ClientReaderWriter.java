package filecsv.writeread;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import filecsv.parsing.ClientConverter;
import model.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class ClientReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(ClientReaderWriter.class);
    @ConfigProperty(propertyName = "csv.clients.import.filePath", type = String.class)
    private static String fileNameImport;
    @ConfigProperty(propertyName = "csv.clients.export.filePath", type = String.class)
    private static String fileNameExport;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeClients(final List<Client> clients) {
        try (FileWriter fileWriter = new FileWriter(new File(fileNameExport))) {
            for (Client client : clients) {
                fileWriter.write(ClientConverter.getStringFromClient(client, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing clients: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<Client> readClients() {
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


