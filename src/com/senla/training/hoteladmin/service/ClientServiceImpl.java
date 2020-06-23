package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repo.*;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.ClientIdProvider;
import com.senla.training.hoteladmin.util.file.ClientParser;
import com.senla.training.hoteladmin.util.sort.ClientsSortCriterion;
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
    private ClientWriter clientWriter;

    private ClientServiceImpl(ArchivService archivService, ClientsRepo clientsRepo,
                             RoomsRepo roomsRepo, ClientWriter clientWriter){
        this.archivService = archivService;
        this.clientsRepo = clientsRepo;
        this.roomsRepo = roomsRepo;
        this.clientWriter = clientWriter;
    }

    public static ClientService getInstance(ArchivService archivService, ClientsRepo clientsRepo,
                                                RoomsRepo roomsRepo, ClientWriter clientWriter) {
        if(instance == null){
            instance = new ClientServiceImpl(archivService,clientsRepo,roomsRepo, clientWriter);
            return instance;
        }
        return instance;
    }

    @Override
    public boolean addResident(Client resident, Date arrival, Date departure) {
        List<Client> residents = clientsRepo.getClients();
        List<Room> rooms = roomsRepo.getRooms();
        boolean hasPlace = false;
        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()){
            Room room = iterator.next();
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

    @Override
    public boolean removeResident(Client resident) {
        List<Room> rooms = roomsRepo.getRooms();
        List<Client> residents = clientsRepo.getClients();
        if(!residents.remove(resident)){
            return false;
        }

        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()){
            Room room = iterator.next();
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

    @Override
    public List<Client> getSortedClients(ClientsSortCriterion criterion) {
        List<Client> clients = clientsRepo.getClients();
        if(criterion.equals(ClientsSortCriterion.ALPHABET)){
            ClientsSorter.sortByAlphabet(clients);
        }
        else if(criterion.equals(ClientsSortCriterion.DEPARTURE_DATE)){
            ClientsSorter.sortByDeparture(clients);
        }
        return clients;
    }

    @Override
    public Integer getNumberOfResidents() {
        return clientsRepo.getClients().size();
    }

    @Override
    public Client getClientById(Integer id) {
        List<Client> clients = clientsRepo.getClients();
        for (Client client:clients){
            if(client.getId().equals(id)){
                return client;
            }
        }
        return null;
    }

    @Override
    public List<Client> getLastThreeResidents(Integer id) {
        return archivService.getLastThreeResidents(id);
    }

    @Override
    public boolean exportClients() {
        try {
            clientWriter.writeClients(clientsRepo.getClients());
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean importClients(RoomService roomService) {
        List<Client> clients;
        try {
            clients = clientWriter.readClients();
            clients.forEach(e->{
                updateClient(e);
                roomService.updateRoom(e.getRoom());
            });
        }
        catch (Exception ex){
            return false;
        }

        return true;
    }

    public void updateClient(Client client){
        if(client == null){
            return;
        }
        List<Client> clients = clientsRepo.getClients();
        int index = clients.indexOf(client);
        if(index == -1){
            client.setId(ClientIdProvider.getNextId());
            clients.add(client);
        }
        else {
            clients.set(index,client);
        }
        clientsRepo.setClients(clients);
    }
}

