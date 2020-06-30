package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.repository.RoomsRepositoryImpl;
import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.service.RoomServiceImpl;
import com.senla.training.hoteladmin.service.RoomWriterImpl;
import com.senla.training.hoteladmin.util.ClientIdProvider;
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

    public String addResident(String firstName, String lastName,
                              Date arrival, Date departure) {
        Client client = new Client(ClientIdProvider.getNextId(), firstName, lastName);
        if (clientService.getClientById(client.getId()) != null) {
            return "Error at adding client: client with this id already exists!";
        }
        if (clientService.addResident(client, arrival, departure)) {
            return "Successfully added resident";
        } else {
            ClientIdProvider.reduceId();
            return "Error at adding client: no free rooms";
        }
    }

    public String removeResident(Integer id) {
        Client client = clientService.getClientById(id);
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
            result.append(e).append("\n");
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
            result.append(e).append("\n");
        });
        return result.toString();
    }

    public String exportClients(){
        if(clientService.exportClients()){
            return "Successfully exported clients";
        }
        else {
            return "Could not export clients";
        }
    }

    public String importClients(){
        if(clientService.importClients( RoomServiceImpl.getInstance(RoomsRepositoryImpl.getInstance(),
                RoomWriterImpl.getInstance()))){
            return "Successfully imported clients";
        }
        else {
            return "Could not import clients";
        }
    }

}

