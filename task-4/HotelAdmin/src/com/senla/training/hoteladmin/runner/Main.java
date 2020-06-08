package com.senla.training.hoteladmin.runner;

import com.senla.training.hoteladmin.model.*;
import com.senla.training.hoteladmin.model.admin.Administrator;
import com.senla.training.hoteladmin.model.archiv.Archivator;
import com.senla.training.hoteladmin.model.archiv.ClientsArchive;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.client.ClientsSortCriterion;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.RoomsSortCriterion;
import com.senla.training.hoteladmin.model.service.Service;
import com.senla.training.hoteladmin.model.service.ServiceSortCriterion;
import com.senla.training.hoteladmin.model.service.ServiceType;
import com.senla.training.hoteladmin.util.DateUtil;

public class Main {

    public static void main(String[] args) throws Exception {
        Hotel hotel = new Hotel(11, 11, 11);
        Administrator admin = new Administrator(hotel, new Archivator(new ClientsArchive(100)));
        Client kostyaClient = new Client(3141456, "Kostya", "Kotov");
        Client petrClient = new Client(3231456, "Petr", "Petrov");
        Client vasiliClient = new Client(5678322, "Vasili", "Volkov");

        admin.addRoom(new Room(1, RoomStatus.SERVED, 100, 3, 5));
        admin.addRoom(new Room(2, RoomStatus.REPAIRED, 50, 2, 4));
        admin.addRoom(new Room(3, RoomStatus.SERVED, 30, 4, 5));
        admin.addRoom(new Room(4, RoomStatus.REPAIRED, 150, 1, 3));
        admin.addResident(kostyaClient, DateUtil.getDate("01.01.2016"),
                DateUtil.getDate("01.02.2016"));
        admin.removeResident(kostyaClient);
        admin.addResident(petrClient, DateUtil.getDate("01.01.2016"),
                DateUtil.getDate("10.02.2016"));
        admin.addResident(vasiliClient, DateUtil.getDate("07.02.2017"),
                DateUtil.getDate("08.04.2017"));

        admin.addService(new Service(10, ServiceType.MASSAGE, petrClient,
                DateUtil.getDate("08.01.2016")));
        admin.addService(new Service(15, ServiceType.SAUNA, vasiliClient,
                DateUtil.getDate("08.02.2017")));
        admin.addService(new Service(5, ServiceType.SPA, petrClient,
                DateUtil.getDate("09.01.2016")));

        System.out.println("All rooms sorted by");
        System.out.println("Price:");
        admin.getSortedRooms(RoomsSortCriterion.PRICE);
        System.out.println("Capacity:");
        admin.getSortedRooms(RoomsSortCriterion.CAPACITY);
        System.out.println("Stars:");
        admin.getSortedRooms(RoomsSortCriterion.STARS);

        System.out.println("Free rooms sorted by");
        System.out.println("Price:");
        admin.getSortedFreeRooms(RoomsSortCriterion.PRICE);
        System.out.println("Capacity:");
        admin.getSortedFreeRooms(RoomsSortCriterion.CAPACITY);
        System.out.println("Stars:");
        admin.getSortedFreeRooms(RoomsSortCriterion.STARS);

        System.out.println("Clients, sorted by:");
        System.out.println("Alphabet:");
        admin.getSortedClients(ClientsSortCriterion.ALPHABET);
        System.out.println("Departure date:");
        admin.getSortedClients(ClientsSortCriterion.DEPARTURE_DATE);

        System.out.println("Number of free rooms: " + admin.getNumberOfFreeRooms());
        System.out.println("Number of clients: " + admin.getNumberOfResidents());

        admin.getFreeRoomsAfterDate(DateUtil.getDate("10.11.2016"));

        System.out.println("Price of room #1 = " + admin.getPriceRoom(1));
        System.out.println("Last residents of room #1:");
        admin.getLast3Residents(1);

        System.out.println("Services of " + petrClient.toString() + "\nSorted by:");
        System.out.println("Price:");
        admin.getClientServices(petrClient, ServiceSortCriterion.PRICE);
        System.out.println("Date:");
        admin.getClientServices(petrClient, ServiceSortCriterion.DATE);

        System.out.println("Services with price:");
        admin.getServices();

        System.out.println("Detailed info for room #1:");
        admin.getRoomInfo(1);
    }
}

