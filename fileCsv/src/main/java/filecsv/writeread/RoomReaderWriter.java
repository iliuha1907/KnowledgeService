package filecsv.writeread;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import filecsv.parsing.RoomConverter;
import model.room.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class RoomReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(RoomReaderWriter.class);
    @ConfigProperty(propertyName = "csv.rooms.import.filePath", type = String.class)
    private static String fileNameImport;
    @ConfigProperty(propertyName = "csv.rooms.export.filePath", type = String.class)
    private static String fileNameExport;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeRooms(final List<Room> rooms) {
        try (FileWriter fileWriter = new FileWriter(new File(fileNameExport))) {
            for (Room room : rooms) {
                fileWriter.write(RoomConverter.getStringFromRoom(room, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing rooms: " + ex.getMessage());
            throw new BusinessException("Could not export rooms!");
        }
    }

    public static List<Room> readRooms() {
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

