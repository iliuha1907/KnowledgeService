package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;

import java.util.Date;
import java.util.List;

public class ClientController {
    private static ClientController instance;
    private ClientService clientService;

    private ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    public static ClientController getInstance(ClientService clientService){
        if(instance == null){
            instance = new ClientController(clientService);
        }
        return instance;
    }

    public String addResident(Integer passportNumber, String firstName, String lastName,
                              Date arrival, Date departure) {
        Client client = new Client(passportNumber, firstName, lastName);
        if (clientService.getClientByPass(passportNumber) != null) {
            return "Error at adding client: client with this passport number already exists!";
        }
        if (clientService.addResident(client, arrival, departure)) {
            return "Successfully added resident";
        } else {
            return "Error at adding client: no free rooms";
        }
    }

    public String removeResident(Integer passportNumber) {
        Client client = clientService.getClientByPass(passportNumber);
        if (client == null) {
            return "Error at removing resident: no such resident!";
        }
        clientService.removeResident(client);
        return "Successfully removed resident";
    }

    public String getSortedClients(ClientsSortCriterion criterion) {
        List<Client> clients = clientService.getSortedClients(criterion);
        String title = "Clients, sorted by " + criterion.toString() + "\n";
        StringBuilder result = new StringBuilder(title);
        clients.forEach(e -> {
            String part = e + "\n";
            result.append(part);
        });
        return result.toString();
    }

    public String getNumberOfResidents() {
        return "Number of residents: " + clientService.getNumberOfResidents();
    }

    public String getLastThreeResidents(int roomNumber) {
        List<Client> clients = clientService.getLastThreeResidents(roomNumber);
        String title = "Last 3 residents of room " + roomNumber + "\n";
        StringBuilder result = new StringBuilder(title);
        clients.forEach(e -> {
            String part = e + "\n";
            result.append(part);
        });
        return result.toString();
    }

}

