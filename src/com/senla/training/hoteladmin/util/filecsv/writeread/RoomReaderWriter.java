package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.filecsv.parsing.RoomConverter;
import com.senla.training.hoteladmin.util.fileproperties.PropertyDataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomReaderWriter {
    private static final String FILE_NAME = PropertyDataProvider.getRoomsCsv();
    private static final String SEPARATOR = PropertyDataProvider.getSeparator();

    public static void writeRooms(List<Room> rooms) {
        try (FileWriter fileWriter = new FileWriter(new File(FILE_NAME))) {
            for (Room room : rooms) {
                fileWriter.write(RoomConverter.getStringFromRoom(room, SEPARATOR) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException("Could not export rooms!");
        }
    }

    public static List<Room> readRooms() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)))) {
            List<Room> rooms = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rooms.add(RoomConverter.parseRoom(line, SEPARATOR));
            }
            return rooms;
        } catch (Exception ex) {
            throw new BusinessException("Could not read rooms");
        }
    }
}

