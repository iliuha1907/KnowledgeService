package com.senla.training.hotelAdmin.service;

import com.senla.training.hotelAdmin.exception.BusinessException;
import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.repository.*;
import com.senla.training.hotelAdmin.util.fileCsv.writeRead.ClientWriter;
import com.senla.training.hotelAdmin.util.sort.ClientsSortCriterion;
import com.senla.training.hotelAdmin.util.sort.ClientsSorter;

import java.util.Date;
import java.util.List;

public class ClientServiceImpl implements ClientService {
    private static ClientService instance;
    private static final Integer LAST_RESIDENTS = 3;
    private HotelServiceRepository hotelServiceRepository;
    private ClientsRepository clientsRepository;
    private RoomsRepository roomsRepository;

    private ClientServiceImpl() {
        this.hotelServiceRepository = HotelServiceRepositoryImpl.getInstance();
        this.clientsRepository = ClientsRepositoryImpl.getInstance();
        this.roomsRepository = RoomsRepositoryImpl.getInstance();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientServiceImpl();
            return instance;
        }
        return instance;
    }

    @Override
    public void addResident(String firstName, String lastName, Date arrival, Date departure) {
        Room room = roomsRepository.getFirstFreeRoom();
        if (room == null) {
            throw new BusinessException("Error at adding clients: no more free rooms!");
        }

        Client client = new Client(null, firstName, lastName, arrival, departure);
        clientsRepository.addClient(client);
        client.setRoom(room);
        room.setResident(client);
    }

    @Override
    public void removeResident(Client resident) {
        clientsRepository.removeClient(resident);

        Room room = roomsRepository.getClientRoom(resident.getId());
        room.setResident(null);

        clientsRepository.addMovedClient(resident);

        hotelServiceRepository.removeClientHotelServices(resident.getId());
    }

    @Override
    public void removeResidentById(Integer id) {
        Client client = getClientById(id);
        removeResident(client);
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
        return clientsRepository.getClientById(id);
    }

    @Override
    public List<Client> getLastThreeResidents(Integer roomId) {
        return clientsRepository.getLastRoomClients(roomId, LAST_RESIDENTS);
    }

    @Override
    public void exportClients() {
        ClientWriter.writeClients(clientsRepository.getClients());
    }

    @Override
    public void importClients() {
        List<Client> clients = ClientWriter.readClients();
        if (clients == null) {
            throw new BusinessException("Could not read clients");
        }
        clients.forEach(client -> {
            Room existing = roomsRepository.getRoom(client.getRoom().getId());
            if (existing == null || existing.getResident() != null) {
                throw new BusinessException("Could not read clients, incorrect id of a room");
            }
            existing.setResident(client);
            client.setRoom(existing);
            updateClient(client);
        });
    }

    public void updateClient(Client client) {
        if (client == null) {
            return;
        }
        List<Client> clients = clientsRepository.getClients();
        int index = clients.indexOf(client);
        if (index == -1) {
            clientsRepository.addClient(client);
        } else {
            clients.set(index, client);
        }
    }
}

