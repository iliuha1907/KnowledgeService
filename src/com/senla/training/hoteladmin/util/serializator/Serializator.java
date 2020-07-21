package com.senla.training.hoteladmin.util.serializator;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.room.Room;
import injection.annotation.ConfigProperty;
import injection.annotation.NeedInjectionClass;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@NeedInjectionClass
public class Serializator {
    @ConfigProperty(propertyName = "dat.roomClients.filePath", type = String.class)
    private static String fileNameRoomsClients;
    @ConfigProperty(propertyName = "dat.services.filePath", type = String.class)
    private static String fileNameServices;
    @ConfigProperty(propertyName = "dat.movedClients.filePath", type = String.class)
    private static String fileNameMovedClients;
    @ConfigProperty(propertyName = "dat.rooms.id.filePath", type = String.class)
    private static String fileNameRoomsId;
    @ConfigProperty(propertyName = "dat.clients.id.filePath", type = String.class)
    private static String fileNameClientsId;
    @ConfigProperty(propertyName = "dat.services.id.filePath", type = String.class)
    private static String fileNameServicesId;

    public static void serializeRoomsClients(List<Room> rooms) {
        try (FileOutputStream outputStream = new FileOutputStream(fileNameRoomsClients);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(rooms);

        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of rooms and clients");
        }

    }

    public static void serializeServices(List<HotelService> services) {
        try (FileOutputStream outputStream = new FileOutputStream(fileNameServices);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(services);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of rooms and clients");
        }
    }

    public static void serializeMovedClients(List<Client> clients) {
        try (FileOutputStream outputStream = new FileOutputStream(fileNameMovedClients);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(clients);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of rooms and clients");
        }
    }

    public static void serializeClientId(Integer id) {
        try (FileOutputStream outputStream = new FileOutputStream(fileNameClientsId);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(id);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of ClientId");
        }
    }

    public static void serializeRoomId(Integer id) {
        try (FileOutputStream outputStream = new FileOutputStream(fileNameRoomsId);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(id);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of RoomId");
        }
    }

    public static void serializeHotelServiceId(Integer id) {
        try (FileOutputStream outputStream = new FileOutputStream(fileNameServicesId);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(id);
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at serialization of HotelServiceId");
        }
    }
}

