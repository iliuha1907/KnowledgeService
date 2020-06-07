package com.senla.training.hoteladmin.model;


import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.service.Service;

public class Hotel {
    private final int DEFAULT_COUNT = 5;
    private Service[] services;
    private Room[] rooms;
    private Client[] clients;

    public Hotel(){
        services = new Service[DEFAULT_COUNT];
        rooms = new Room[DEFAULT_COUNT];
        clients = new Client[DEFAULT_COUNT];
    }

    public Hotel(int numberOfServices, int numberOfRooms, int numberOfClients){
        services = new Service[numberOfServices];
        rooms = new Room[numberOfRooms];
        clients = new Client[numberOfClients];
    }

    public void setServices(Service[] services){
        this.services = services;
    }

    public void setRooms(Room[] rooms){
        this.rooms = rooms;
    }

    public void setClients(Client[] clients) {
        this.clients = clients;
    }

    public Service[] getServices() {
        return services;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public Client[] getClients() {
        return clients;
    }
}
