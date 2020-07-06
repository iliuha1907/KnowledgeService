package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;

import java.util.Date;
import java.util.List;

public class ClientController {
    private static ClientController instance;
    private ClientService clientService;

    private ClientController() {
        this.clientService = ClientServiceImpl.getInstance();
    }

    public static ClientController getInstance() {
        if (instance == null) {
            instance = new ClientController();
        }
        return instance;
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
        try {
            clientService.deserializeMovedClients();
            return "Successful deserialization of last residents";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String serializeLastResidents() {
        try {
            clientService.serializeMovedClients();
            return "Successful serialization of last residents";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String deserializeClientsId() {
        try {
            clientService.deserializeId();
            return "Successful deserialization of client id";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String serializeClientId() {
        try {
            clientService.serializeId();
            return "Successful serialization of client id";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

}

