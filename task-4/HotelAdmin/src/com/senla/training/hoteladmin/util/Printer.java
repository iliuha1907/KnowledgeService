package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.service.Service;

public class Printer {

    public static void printRooms(Room[] rooms) {
        System.out.println("Rooms:");
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                break;
            }
            System.out.println(rooms[i]);
        }
    }

    public static void printClients(Client[] clients) {
        System.out.println("Clients:");
        for (int i = 0; i < clients.length; i++) {
            if (clients[i] == null) {
                break;
            }
            System.out.println(clients[i]);
        }
    }

    public static void printServices(Service[] services) {
        System.out.println("Services:");
        for (int i = 0; i < services.length; i++) {
            if (services[i] == null) {
                break;
            }
            System.out.println(services[i]);
        }
    }
}

