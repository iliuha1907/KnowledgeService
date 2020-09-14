package com.senla.training.hoteladmin.csvapi.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.parsing.RoomConverter;
import com.senla.training.hoteladmin.model.room.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoomReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(RoomReaderWriter.class);
    @Value("${csv.rooms.import.filePath:csv/rooms.csv}")
    private String fileNameImport;
    @Value("${csv.rooms.export.filePath:exportCsv/rooms.csv}")
    private String fileNameExport;
    @Value("${csv.separator:;}")
    private String separator;

    public void writeRooms(final List<Room> rooms) {
        try (FileWriter fileWriter = new FileWriter(new File(fileNameExport))) {
            for (Room room : rooms) {
                fileWriter.write(RoomConverter.getStringFromRoom(room, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing rooms: " + ex.getMessage());
            throw new BusinessException("Could not export rooms!");
        }
    }

    public List<Room> readRooms() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                ClientReaderWriter.class.getClassLoader().getResource(fileNameImport).getFile())))) {
            List<Room> rooms = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                rooms.add(RoomConverter.parseRoom(line, separator));
            }
            return rooms;
        } catch (Exception ex) {
            LOGGER.error("Error at reading rooms: " + ex.getMessage());
            throw new BusinessException("Could not read rooms");
        }
    }
}

