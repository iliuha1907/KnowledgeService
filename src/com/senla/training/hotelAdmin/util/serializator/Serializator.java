package com.senla.training.hotelAdmin.util.serializator;


import com.senla.training.hotelAdmin.exception.BusinessException;
import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.model.hotelService.HotelService;
import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.util.fileProperties.PropertyDataProvider;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class Serializator {
    private static final String FILE_NAME_ROOMS_CLIENTS = PropertyDataProvider.getRoomClientsFile();
    private static final String FILE_NAME_SERVICES = PropertyDataProvider.getServicesFile();
    private static final String FILE_NAME_MOVED_CLIENTS = PropertyDataProvider.getMovedClientsFile();

    public static void serializeRoomsClients(List<Room> rooms) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_ROOMS_CLIENTS);) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);) {
                objectOutputStream.writeObject(rooms);
            }
        } catch (Exception ex) {
            throw new BusinessException("Error at serialization of rooms and clients");
        }

    }

    public static void serializeServices(List<HotelService> services) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_SERVICES);) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);) {
                objectOutputStream.writeObject(services);
            }
        } catch (Exception ex) {
            throw new BusinessException("Error at serialization of rooms and clients");
        }
    }

    public static void serializeMovedClients(List<Client> clients) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_MOVED_CLIENTS);) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);) {
                objectOutputStream.writeObject(clients);
            }
        } catch (Exception ex) {
            throw new BusinessException("Error at serialization of rooms and clients");
        }
    }
}
