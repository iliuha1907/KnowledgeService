package com.senla.training.hotelAdmin.util.serializator;

import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.model.hotelService.HotelService;
import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.util.fileProperties.PropertyDataProvider;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;


public class Deserializator {
    private static final String FILE_NAME_ROOMS_CLIENTS = PropertyDataProvider.getRoomClientsFile();
    private static final String FILE_NAME_SERVICES = PropertyDataProvider.getServicesFile();
    private static final String FILE_NAME_MOVED_CLIENTS = PropertyDataProvider.getMovedClientsFile();


    public static List<Room> deserializeRoomClients() {
        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME_ROOMS_CLIENTS)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                List<Room> rooms = (List<Room>) objectInputStream.readObject();
                return rooms;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static List<Client> deserializeMovedClients() {
        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME_MOVED_CLIENTS)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                List<Client> clients = (List<Client>) objectInputStream.readObject();
                return clients;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static List<HotelService> deserializeServices() {
        try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME_SERVICES)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                List<HotelService> hotelServices = (List<HotelService>) objectInputStream.readObject();
                return hotelServices;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}

