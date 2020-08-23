package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.service.ClientService;

import java.util.List;

@NeedInjectionClass
public class ClientController {
    @NeedInjectionField
    private ClientService clientService;

    public String addClient(String firstName, String lastName) {
        try {
            clientService.addClient(firstName, lastName);
            return "Successfully added client";
        } catch (Exception ex) {
            return "Error at adding client: " + ex.getMessage();
        }
    }

    public String getClients(){
        List<Client> clients;
        try {
            clients = clientService.getClients();
        } catch (Exception ex) {
            return "Error at getting rooms: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Clients:\n");
        clients.forEach(client -> {
            result.append(client).append("\n");
        });
        return result.toString();
    }
}

