package com.senla.training.hoteladmin.util.serializator;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.fileproperties.PropertyDataProvider;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class Deserializator {
    private static final String FILE_NAME_ROOMS_CLIENTS = PropertyDataProvider.getRoomClientsFile();
    private static final String FILE_NAME_SERVICES = PropertyDataProvider.getServicesFile();
    private static final String FILE_NAME_MOVED_CLIENTS = PropertyDataProvider.getMovedClientsFile();
    private static final String FILE_NAME_ROOMS_ID = PropertyDataProvider.getRoomIdFile();
    private static final String FILE_NAME_CLIENTS_ID = PropertyDataProvider.getClientIdFile();
    private static final String FILE_NAME_SERVICES_ID = PropertyDataProvider.getHotelServiceIdFile();

    @SuppressWarnings("unchecked")
    public static List<Room> deserializeRoomClients() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(FILE_NAME_ROOMS_CLIENTS))) {
            return (List<Room>) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of rooms and clients");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Client> deserializeMovedClients() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(FILE_NAME_MOVED_CLIENTS))) {
            return (List<Client>) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of moved clients");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<HotelService> deserializeServices() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(FILE_NAME_SERVICES))) {
            return (List<HotelService>) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of hotel services");
        }
    }

    public static Integer deserializeClientId() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(FILE_NAME_CLIENTS_ID))) {
            return (Integer) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of client idspread");
        }
    }

    public static Integer deserializeRoomId() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(FILE_NAME_ROOMS_ID))) {
            return (Integer) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of room idspread");
        }
    }

    public static Integer deserializeHotelServiceId() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream(FILE_NAME_SERVICES_ID))) {
            return (Integer) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new IncorrectWorkException("Error at deserialization of hotel services idspread");
        }
    }
}

