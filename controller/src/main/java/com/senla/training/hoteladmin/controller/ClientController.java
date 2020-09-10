package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionField;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.service.ClientService;

import java.util.List;

@NeedInjectionClass
public class ClientController {

    @NeedInjectionField
    private ClientService clientService;

    public String addClient(final String firstName, final String lastName) {
        try {
            clientService.addClient(firstName, lastName);
            return "Successfully added client";
        } catch (Exception ex) {
            return "Error at adding client: " + ex.getMessage();
        }
    }

    public String getClients() {
        List<Client> clients;
        try {
            clients = clientService.getClients();
        } catch (Exception ex) {
            return "Error at getting rooms: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Clients:\n");
        clients.forEach(client ->
                result.append(client).append("\n")
        );
        return result.toString();
    }

    public String getNumberOfClients() {
        try {
            return clientService.getNumberOfClients().toString();
        } catch (Exception ex) {
            return "Error at getting number of clients: " + ex.getMessage();
        }
    }

    public String exportClients() {
        try {
            clientService.exportClients();
            return "Successfully exported clients";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String importClients() {
        try {
            clientService.importClients();
            return "Successfully imported clients";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }
}
