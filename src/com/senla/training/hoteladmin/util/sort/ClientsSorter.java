package com.senla.training.hoteladmin.util.sort;

import com.senla.training.hoteladmin.model.client.Client;

import java.util.List;

public class ClientsSorter {
    public static void sortByAlphabet(List<Client> clients) {
        clients.sort((o1, o2) -> {
            if ((o1.getFirstName() == null || o1.getLastName() == null) &&
                    (o2.getFirstName() == null || o2.getLastName() == null)) {
                return 0;
            }
            if ((o1.getFirstName() == null || o1.getLastName() == null) &&
                    !(o2.getFirstName() == null || o2.getLastName() == null)) {
                return -1;
            }
            if (!(o1.getFirstName() == null || o1.getLastName() == null) &&
                    (o2.getFirstName() == null || o2.getLastName() == null)) {
                return 1;
            }
            String fullName1 = o1.getFirstName() + " " + o1.getLastName();
            String fullName2 = o2.getFirstName() + " " + o2.getLastName();
            return fullName1.compareTo(fullName2);
        });
    }

    public static void sortByDeparture(List<Client> clients) {
        clients.sort((o1, o2) -> {
            if (o1.getDepartureDate() == null && o2.getDepartureDate() == null) {
                return 0;
            }
            if (o1.getDepartureDate() == null && o2.getDepartureDate() != null) {
                return -1;
            }
            if (o1.getDepartureDate() != null && o2.getDepartureDate() == null) {
                return 1;
            }
            return o1.getDepartureDate().compareTo(o2.getDepartureDate());
        });
    }
}

