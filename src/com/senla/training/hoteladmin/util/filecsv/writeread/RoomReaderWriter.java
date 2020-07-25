package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.filecsv.parsing.RoomConverter;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class RoomReaderWriter {
    @ConfigProperty(propertyName = "csv.rooms.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeRooms(List<Room> rooms) {
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            for (Room room : rooms) {
                fileWriter.write(RoomConverter.getStringFromRoom(room, separator) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException("Could not export rooms!");
        }
    }

    public static List<Room> readRooms() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            List<Room> rooms = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rooms.add(RoomConverter.parseRoom(line, separator));
            }
            return rooms;
        } catch (Exception ex) {
            throw new BusinessException("Could not read rooms");
        }
    }
}

