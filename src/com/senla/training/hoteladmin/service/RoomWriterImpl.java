package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.file.ClientParser;
import com.senla.training.hoteladmin.util.file.RoomParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomWriterImpl implements RoomWriter {
    private static RoomWriterImpl instance;

    private RoomWriterImpl() {
    }

    public static RoomWriter getInstance() {
        if (instance == null) {
            instance = new RoomWriterImpl();
        }
        return instance;
    }

    @Override
    public void writeRooms(List<Room> rooms) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(FILE_NAME));
        for (Room room : rooms) {
            fileWriter.write(RoomParser.getStringFromRoom(room, SEPARATOR));
            fileWriter.write(ClientParser.getStringFromResident(room.getResident(), SEPARATOR) + "\n");
        }
        fileWriter.close();
    }

    @Override
    public List<Room> readRooms() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)));
        List<Room> rooms = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            rooms.add(RoomParser.parseRoom(line, SEPARATOR));
        }
        reader.close();
        return rooms;
    }
}

