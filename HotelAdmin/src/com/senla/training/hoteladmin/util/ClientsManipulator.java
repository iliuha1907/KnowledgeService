package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.Arrays;
import java.util.Comparator;

public class ClientsManipulator {

    public static void sortByAlphabet(Client[] clients){
        Arrays.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                String fullName1 = o1.getFirstName()+" "+o1.getLastName();
                String fullName2 = o2.getFirstName()+" "+o2.getLastName();
                return fullName1.compareTo(fullName2);
            }
        });
    }

    public static void sortByDeparture(Client[] clients){
        Arrays.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
               return o1.getDepartureDate().compareTo(o2.getDepartureDate());
            }
        });
    }


    public static Client[] getRealClients(Client[] clients, int count){
        Client[] realClients = new Client[count];
        for(int i =0;i<clients.length;i++){
            if(clients[i] == null){
                break;
            }
           realClients[i] = clients[i];
        }
        return realClients;
    }
}
