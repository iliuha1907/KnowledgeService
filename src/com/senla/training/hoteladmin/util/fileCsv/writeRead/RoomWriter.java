package com.senla.training.hotelAdmin.util.fileCsv.writeRead;

import com.senla.training.hotelAdmin.model.room.Room;
import com.senla.training.hotelAdmin.util.fileCsv.parsing.RoomParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomWriter {
    private final static String FILE_NAME = "Files/rooms.csv";
    private final static String SEPARATOR = ";";

    public static boolean writeRooms(List<Room> rooms) {
        try (FileWriter fileWriter = new FileWriter(new File(FILE_NAME))) {
            for (Room room : rooms) {
                fileWriter.write(RoomParser.getStringFromRoom(room, SEPARATOR));
                fileWriter.write(room.getResident().getId() + SEPARATOR + "\n");
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static List<Room> readRooms() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)))) {
            List<Room> rooms = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rooms.add(RoomParser.parseRoom(line, SEPARATOR));
            }
            return rooms;
        } catch (Exception ex) {
            return null;
        }
    }
}

