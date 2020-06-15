package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repo.*;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.client.ClientsSortCriterion;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.sort.ClientsSorter;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;

public class ClientServiceImpl implements ClientService {
    private static ClientServiceImpl instance;
    private ArchivService archivService;
    private ClientsRepo clientsRepo;
    private RoomsRepo roomsRepo;

    private ClientServiceImpl(ArchivService archivService, ClientsRepo clientsRepo,
                             RoomsRepo roomsRepo){
        this.archivService = archivService;
        this.clientsRepo = clientsRepo;
        this.roomsRepo = roomsRepo;
    }

    public static ClientServiceImpl getInstance(ArchivService archivService, ClientsRepo clientsRepo,
                                                RoomsRepo roomsRepo) {
        if(instance == null){
            instance = new ClientServiceImpl(archivService,clientsRepo,roomsRepo);
            return instance;
        }
        return instance;
    }

    public boolean addResident(Client resident, Date arrival, Date departure) {
        List<Client> residents = clientsRepo.getClients();
        List<Room> rooms = roomsRepo.getRooms();
        boolean hasPlace = false;
        ListIterator iterator = rooms.listIterator();
        while (iterator.hasNext()){
            Room room = (Room)iterator.next();
            if (room.getResident() == null && room.getStatus() != RoomStatus.REPAIRED) {
                hasPlace = true;
                resident.setArrivalDate(arrival);
                resident.setDepartureDate(departure);
                resident.setRoom(room);
                room.setResident(resident);
                roomsRepo.setRooms(rooms);
                residents.add(resident);
                clientsRepo.setClients(residents);
                break;
            }
        }

        if (!hasPlace) {
            return false;
        }
        return true;
    }

    public boolean removeResident(Client resident) {
        List<Room> rooms = roomsRepo.getRooms();
        List<Client> residents = clientsRepo.getClients();
        if(!residents.remove(resident)){
            return false;
        }

        ListIterator iterator = rooms.listIterator();
        while (iterator.hasNext()){
            Room room = (Room)iterator.next();
            if(room.getResident() == null){
                continue;
            }
            if (room.getResident().equals(resident)) {
                room.setResident(null);
                break;
            }
        }

        roomsRepo.setRooms(rooms);
        clientsRepo.setClients(residents);
        archivService.addClient(resident);
        return true;
    }

    public List<Client> getSortedClients(ClientsSortCriterion criterion) {
        List<Client> clients = clientsRepo.getClients();
        switch (criterion) {
            case ALPHABET:
                ClientsSorter.sortByAlphabet(clients);
                break;
            case DEPARTURE_DATE:
                ClientsSorter.sortByDeparture(clients);
                break;
        }
        return clients;
    }

    public Integer getNumberOfResidents() {
        return clientsRepo.getClients().size();
    }

    @Override
    public Client getClientByPass(Integer passNummber) {
        List<Client> clients = clientsRepo.getClients();
        for (Client client:clients){
            if(client.getPassportNumber().equals(passNummber)){
                return client;
            }
        }
        return null;
    }

    @Override
    public List<Client> getLast3Residents(Integer roomNumber) {
        return archivService.getLast3Residents(roomNumber);
    }
}

