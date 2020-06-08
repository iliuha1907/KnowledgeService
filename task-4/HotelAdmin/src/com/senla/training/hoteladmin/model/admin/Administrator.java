package com.senla.training.hoteladmin.model.admin;

import com.senla.training.hoteladmin.model.Hotel;
import com.senla.training.hoteladmin.model.archiv.Archivator;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.client.ClientsSortCriterion;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.model.room.RoomsSortCriterion;
import com.senla.training.hoteladmin.model.service.Service;
import com.senla.training.hoteladmin.model.service.ServiceSortCriterion;
import com.senla.training.hoteladmin.model.service.ServiceType;
import com.senla.training.hoteladmin.util.ClientsManipulator;
import com.senla.training.hoteladmin.util.Printer;
import com.senla.training.hoteladmin.util.RoomsManipulator;
import com.senla.training.hoteladmin.util.ServiceManipulator;

import java.util.Date;

public class Administrator {
    private int currentService = 0, currentRoom = 0, currentClient = 0;
    private int freeRooms = 0;
    private Hotel hotel;
    private Archivator archivator;

    public Administrator(Hotel hotel, Archivator archivator) {
        this.hotel = hotel;
        this.archivator = archivator;
    }

    public void addService(Service service) {
        Service[] services = hotel.getServices();
        if (currentService == services.length) {
            throw new IllegalStateException("No more space for services!");
        }

        services[currentService++] = service;
        hotel.setServices(services);
    }

    public void addRoom(Room room) {
        Room[] rooms = hotel.getRooms();
        if (currentRoom == rooms.length) {
            throw new IllegalStateException("No more space for rooms!");
        }

        rooms[currentRoom++] = room;
        hotel.setRooms(rooms);
        freeRooms++;
    }

    public void addResident(Client resident, Date arrival, Date departure) {
        Client[] residents = hotel.getClients();
        if (currentClient == residents.length) {
            throw new IllegalStateException("No more free rooms");
        }

        Room[] rooms = hotel.getRooms();
        boolean hasPlace = false;
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                break;
            }
            if (rooms[i].getResident() == null && rooms[i].getStatus() != RoomStatus.REPAIRED) {
                hasPlace = true;
                resident.setArrivalDate(arrival);
                resident.setDepartureDate(departure);
                resident.setRoom(rooms[i]);
                rooms[i].setResident(resident);
                hotel.setRooms(rooms);
                residents[currentClient++] = resident;
                hotel.setClients(residents);
                freeRooms--;
                break;
            }
        }

        if (!hasPlace) {
            throw new IllegalStateException("No more free rooms!");
        }
    }

    public void removeResident(Client resident) {
        Room[] rooms = hotel.getRooms();
        Client[] residents = hotel.getClients();
        int indexOfClient = -1;
        for (int i = 0; i < residents.length; i++) {
            if(residents[i] == null){
                throw new IllegalArgumentException("No such client!");
            }
            if (residents[i].equals(resident)) {
                indexOfClient = i;
                break;
            }
        }

        for (int i = indexOfClient; i < currentClient; i++) {
            residents[i] = residents[i + 1];
        }
        currentClient--;

        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i].getResident().equals(resident)) {
                rooms[i].setResident(null);
                break;
            }
        }
        hotel.setRooms(rooms);
        hotel.setClients(residents);
        archivator.addClient(resident);
        freeRooms++;
    }

    public void setRoomStatus(int roomNumber, RoomStatus status) {
        Room[] rooms = hotel.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                throw new RuntimeException("No such room!");
            }
            if (rooms[i].getNumber() == roomNumber) {
                rooms[i].setStatus(status);
                break;
            }
        }
        hotel.setRooms(rooms);
    }

    public void setRoomPrice(int roomNumber, double price) {
        Room[] rooms = hotel.getRooms();
        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                throw new RuntimeException("No such room!");
            }
            if (rooms[i].getNumber() == roomNumber) {
                rooms[i].setPrice(price);
            }
        }
        hotel.setRooms(rooms);
    }

    public void setServicePrice(ServiceType type, double price) {
        Service[] services = hotel.getServices();
        for (int i = 0; i < services.length; i++) {
            if (services[i] == null) {
                throw new RuntimeException("No such service!");
            }
            if (services[i].getType() == type) {
                services[i].setPrice(price);
            }
        }
        hotel.setServices(services);
    }

    public void getSortedRooms(RoomsSortCriterion criterion) {
        Room[] rooms = RoomsManipulator.getRealRooms(hotel.getRooms(), currentRoom);
        switch (criterion) {
            case PRICE:
                RoomsManipulator.sortRoomsByPrice(rooms);
                break;
            case STARS:
                RoomsManipulator.sortRoomsByCapacity(rooms);
                break;
            case CAPACITY:
                RoomsManipulator.sortRoomsByStars(rooms);
                break;
        }
        Printer.printRooms(rooms);
    }

    public void getSortedFreeRooms(RoomsSortCriterion criterion) {
        Room[] free = RoomsManipulator.getFreeRooms(hotel.getRooms(), freeRooms);
        switch (criterion) {
            case PRICE:
                RoomsManipulator.sortRoomsByPrice(free);
                break;
            case STARS:
                RoomsManipulator.sortRoomsByCapacity(free);
                break;
            case CAPACITY:
                RoomsManipulator.sortRoomsByStars(free);
                break;
        }
        Printer.printRooms(free);
    }

    public void getSortedClients(ClientsSortCriterion criterion) {
        Client[] clients = ClientsManipulator.getRealClients(hotel.getClients(), currentClient);
        switch (criterion) {
            case ALPHABET:
                ClientsManipulator.sortByAlphabet(clients);
                break;
            case DEPARTURE_DATE:
                ClientsManipulator.sortByDeparture(clients);
                break;
        }
        Printer.printClients(clients);
    }

    public void getFreeRoomsAfterDate(Date date) {
        RoomsManipulator.displayFreeRoomsAfterDate(hotel.getRooms(), date);
    }

    public double getPriceRoom(int roomNumber) {
        double price = RoomsManipulator.getRoomPrice(hotel.getRooms(), roomNumber);
        if (price == -1) {
            throw new RuntimeException("No such room");
        }
        return price;
    }

    public void getLast3Residents(int roomNumber) {
        archivator.displayLast3Residents(roomNumber);
    }

    public void getClientServices(Client client, ServiceSortCriterion criterion) {
        Service[] services = ServiceManipulator.getClientServices(hotel.getServices(), client);
        switch (criterion) {
            case DATE:
                ServiceManipulator.sortByDate(services);
                break;
            case PRICE:
                ServiceManipulator.sortByPrice(services);
                break;
        }
        Printer.printServices(services);
    }

    public void getServices() {
        Printer.printServices(hotel.getServices());
    }

    public void getRoomInfo(int roomNumber) {
        RoomsManipulator.displayRoom(hotel.getRooms(), roomNumber);
    }

    public int getNumberOfResidents() {
        return currentClient;
    }

    public int getNumberOfFreeRooms() {
        return freeRooms;
    }
}

