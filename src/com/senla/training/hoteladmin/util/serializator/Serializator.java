package com.senla.training.hoteladmin.util.serializator;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.fileproperties.PropertyDataProvider;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class Serializator {
    private static final String FILE_NAME_ROOMS_CLIENTS = PropertyDataProvider.getRoomClientsFile();
    private static final String FILE_NAME_SERVICES = PropertyDataProvider.getServicesFile();
    private static final String FILE_NAME_MOVED_CLIENTS = PropertyDataProvider.getMovedClientsFile();
    private static final String FILE_NAME_ROOMS_ID = PropertyDataProvider.getRoomIdFile();
    private static final String FILE_NAME_CLIENTS_ID = PropertyDataProvider.getClientIdFile();
    private static final String FILE_NAME_SERVICES_ID = PropertyDataProvider.getHotelServiceIdFile();

    public static void serializeRoomsClients(List<Room> rooms) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_ROOMS_CLIENTS);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(rooms);

        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of rooms and clients");
        }

    }

    public static void serializeServices(List<HotelService> services) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_SERVICES);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(services);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of rooms and clients");
        }
    }

    public static void serializeMovedClients(List<Client> clients) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_MOVED_CLIENTS);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(clients);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of rooms and clients");
        }
    }

    public static void serializeClientId(Integer id) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_CLIENTS_ID);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(id);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of ClientId");
        }
    }

    public static void serializeRoomId(Integer id) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_ROOMS_ID);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(id);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of RoomId");
        }
    }

    public static void serializeHotelServiceId(Integer id) {
        try (FileOutputStream outputStream = new FileOutputStream(FILE_NAME_SERVICES_ID);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(id);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of HotelServiceId");
        }
    }
}

