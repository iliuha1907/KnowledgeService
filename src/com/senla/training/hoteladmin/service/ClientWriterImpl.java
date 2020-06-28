package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.file.ClientParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientWriterImpl implements ClientWriter {
    private static ClientWriterImpl instance;

    private ClientWriterImpl() {
    }

    public static ClientWriter getInstance() {
        if (instance == null) {
            instance = new ClientWriterImpl();
        }
        return instance;
    }

    @Override
    public void writeClients(List<Client> clients) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(FILE_NAME));
        for (Client client : clients) {
            fileWriter.write(ClientParser.getStringFromResident(client, SEPARATOR));
            fileWriter.write(client.getRoom().getId() + SEPARATOR + "\n");
        }
        fileWriter.close();
    }

    @Override
    public List<Client> readClients() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)));
        List<Client> clients = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            clients.add(ClientParser.parseClient(line, SEPARATOR));
        }
        reader.close();
        return clients;
    }
}

