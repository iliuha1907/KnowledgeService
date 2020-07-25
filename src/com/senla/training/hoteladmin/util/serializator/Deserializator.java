package com.senla.training.hoteladmin.util.serializator;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

@NeedInjectionClass
public class Deserializator {
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

    @SuppressWarnings("unchecked")
    public static List<Room> deserializeRoomClients() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileNameRoomsClients))) {
            return (List<Room>) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of rooms and clients");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Client> deserializeMovedClients() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileNameMovedClients))) {
            return (List<Client>) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of moved clients");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<HotelService> deserializeServices() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileNameServices))) {
            return (List<HotelService>) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of hotel services");
        }
    }

    public static Integer deserializeClientId() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileNameClientsId))) {
            return (Integer) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of client idspread");
        }
    }

    public static Integer deserializeRoomId() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileNameRoomsId))) {
            return (Integer) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of room idspread");
        }
    }

    public static Integer deserializeHotelServiceId() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(fileNameServicesId))) {
            return (Integer) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of hotel services idspread");
        }
    }
}

