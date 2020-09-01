package service;


import model.client.Client;

import java.util.List;

public interface ClientService {

    void addClient(String firstName, String lastName);

    List<Client> getClients();

    Integer getNumberOfClients();

    void exportClients();

    void importClients();
}
