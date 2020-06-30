package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repository.*;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.service.writer.ClientWriter;
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
    private HotelServiceService hotelServiceService;
    private ClientsRepository clientsRepository;
    private RoomsRepository roomsRepository;
    private ClientWriter clientWriter;

    private ClientServiceImpl(ArchivService archivService, HotelServiceService hotelServiceService, ClientsRepository clientsRepository,
                              RoomsRepository roomsRepository, ClientWriter clientWriter) {
        this.archivService = archivService;
        this.hotelServiceService = hotelServiceService;
        this.clientsRepository = clientsRepository;
        this.roomsRepository = roomsRepository;
        this.clientWriter = clientWriter;
    }

    public static ClientService getInstance(ArchivService archivService, HotelServiceService hotelServiceService,
                                            ClientsRepository clientsRepository, RoomsRepository roomsRepository, ClientWriter clientWriter) {
        if (instance == null) {
            instance = new ClientServiceImpl(archivService, hotelServiceService, clientsRepository, roomsRepository, clientWriter);
            return instance;
        }
        return instance;
    }

    @Override
    public boolean addResident(Client resident, Date arrival, Date departure) {
        List<Client> residents = clientsRepository.getClients();
        List<Room> rooms = roomsRepository.getRooms();
        boolean hasPlace = false;
        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if (room.getResident() == null && room.getStatus() != RoomStatus.REPAIRED) {
                hasPlace = true;
                resident.setArrivalDate(arrival);
                resident.setDepartureDate(departure);
                resident.setRoom(room);
                room.setResident(resident);
                roomsRepository.setRooms(rooms);
                residents.add(resident);
                clientsRepository.setClients(residents);
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
        List<Room> rooms = roomsRepository.getRooms();
        List<Client> residents = clientsRepository.getClients();
        if (!residents.remove(resident)) {
            return false;
        }

        ListIterator<Room> iterator = rooms.listIterator();
        while (iterator.hasNext()) {
            Room room = iterator.next();
            if (room.getResident() == null) {
                continue;
            }
            if (room.getResident().equals(resident)) {
                room.setResident(null);
                break;
            }
        }

        roomsRepository.setRooms(rooms);
        clientsRepository.setClients(residents);
        archivService.addClient(resident);
        hotelServiceService.removeService(resident.getId());
        return true;
    }

    @Override
    public List<Client> getSortedClients(ClientsSortCriterion criterion) {
        List<Client> clients = clientsRepository.getClients();
        if (criterion.equals(ClientsSortCriterion.ALPHABET)) {
            ClientsSorter.sortByAlphabet(clients);
        } else if (criterion.equals(ClientsSortCriterion.DEPARTURE_DATE)) {
            ClientsSorter.sortByDeparture(clients);
        }
        return clients;
    }

    @Override
    public Integer getNumberOfResidents() {
        return clientsRepository.getClients().size();
    }

    @Override
    public Client getClientById(Integer id) {
        List<Client> clients = clientsRepository.getClients();
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public List<Client> getLastThreeResidents(Integer roomId) {
        return archivService.getLastThreeResidents(roomId);
    }

    @Override
    public boolean exportClients() {
        try {
            clientWriter.writeClients(clientsRepository.getClients());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean importClients() {
        List<Client> clients;
        try {
            clients = clientWriter.readClients();
            clients.forEach(e -> {
                updateClient(e);
            });
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public void updateClient(Client client) {
        if (client == null) {
            return;
        }
        List<Client> clients = clientsRepository.getClients();
        int index = clients.indexOf(client);
        if (index == -1) {
            clients.add(client);
        } else {
            clients.set(index, client);
        }
        clientsRepository.setClients(clients);
    }
}

