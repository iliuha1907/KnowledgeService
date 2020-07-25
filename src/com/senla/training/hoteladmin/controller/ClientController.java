package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;

import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ClientController {
    @NeedInjectionField
    private ClientService clientService;

    public ClientController() {
    }

    public String addResident(String firstName, String lastName,
                              Date arrival, Date departure) {
        try {
            clientService.addResident(firstName, lastName, arrival, departure);
            return "Successfully added resident";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String removeResident(Integer id) {
        try {
            clientService.removeResidentById(id);
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
        return "Successfully removed resident";
    }

    public String getSortedClients(ClientsSortCriterion criterion) {
        List<Client> clients = clientService.getSortedClients(criterion);
        String title = "Clients, sorted by " + criterion.toString() + "\n";
        StringBuilder result = new StringBuilder(title);
        clients.forEach(client -> {
            result.append(client).append("\n");
        });
        return result.toString();
    }

    public String getNumberOfResidents() {
        return "Number of residents: " + clientService.getNumberOfResidents();
    }

    public String getLastResidents(int roomNumber) {
        List<Client> clients = clientService.getLastResidents(roomNumber);
        String title = "Last residents of room " + roomNumber + "\n";
        StringBuilder result = new StringBuilder(title);
        clients.forEach(client -> {
            result.append(client).append("\n");
        });
        return result.toString();
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

    public String deserializeLastResidents() {
        clientService.deserializeMovedClients();
        return "Successful deserialization of last residents";
    }

    public String serializeLastResidents() {
        clientService.serializeMovedClients();
        return "Successful serialization of last residents";
    }

    public String deserializeClientsId() {
        clientService.deserializeId();
        return "Successful deserialization of client idspread";
    }

    public String serializeClientId() {
        clientService.serializeId();
        return "Successful serialization of client idspread";
    }
}

