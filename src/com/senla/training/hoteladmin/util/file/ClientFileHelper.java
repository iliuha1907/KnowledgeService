package com.senla.training.hoteladmin.util.file;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.util.DateUtil;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ClientFileHelper {
    private final static String FILE_NAME = "Files/clients.csv";
    private final static String SEPARATOR = ";";

    public static void writeClients(List<Client> clients) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(FILE_NAME));
        for (Client client : clients) {
            fileWriter.write(getStringFromResident(client));
            fileWriter.write(getStringFromRoom(client.getRoom()) + "\n");
        }
        fileWriter.close();
    }

    public static List<Client> readClients() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)));
        List<Client> clients = new LinkedList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            clients.add(parseClient(line));
        }
        reader.close();
        return clients;
    }

    private static Client parseClient(String data) {
        int startReading = 0;
        String[] fields = data.split(SEPARATOR);
        Integer clientId = Integer.parseInt(fields[startReading++]);
        String clientFirstName = fields[startReading++];
        String clientLastName = fields[startReading++];
        Date clientArrival = DateUtil.getDate(fields[startReading++]);
        Date clientDep = DateUtil.getDate(fields[startReading++]);


        Integer id = Integer.parseInt(fields[startReading++]);
        RoomStatus roomStatus = RoomStatus.valueOf(fields[startReading++]);
        Integer capacity = Integer.parseInt(fields[startReading++]);
        Integer stars = Integer.parseInt(fields[startReading++]);
        BigDecimal price = new BigDecimal(fields[startReading++]);

        Client client = new Client(clientId, clientFirstName, clientLastName, clientArrival, clientDep);
        Room room = new Room(id, roomStatus, price, capacity, stars);
        room.setResident(client);
        client.setRoom(room);
        return client;
    }

    private static String getStringFromRoom(Room room) {
        if (room == null) {
            return "-" + SEPARATOR;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(room.getId());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getStatus());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getCapacity());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getStars());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(room.getPrice());
        stringBuilder.append(SEPARATOR);
        return stringBuilder.toString();
    }

    private static String getStringFromResident(Client client) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(client.getId());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(client.getFirstName());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(client.getLastName());
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(DateUtil.getStr(client.getArrivalDate()));
        stringBuilder.append(SEPARATOR);
        stringBuilder.append(DateUtil.getStr(client.getDepartureDate()));
        stringBuilder.append(SEPARATOR);
        return stringBuilder.toString();
    }
}

