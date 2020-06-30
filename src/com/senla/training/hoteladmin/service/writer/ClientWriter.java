package com.senla.training.hoteladmin.service.writer;

import com.senla.training.hoteladmin.model.client.Client;

import java.io.IOException;
import java.util.List;

public interface ClientWriter {
    String FILE_NAME = "Files/clients.csv";
    String SEPARATOR = ";";

    void writeClients(List<Client> clients) throws IOException;

    List<Client> readClients() throws IOException;
}

