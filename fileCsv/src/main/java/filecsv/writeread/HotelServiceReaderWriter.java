package filecsv.writeread;

import annotation.ConfigProperty;
import annotation.NeedInjectionClass;
import exception.BusinessException;
import filecsv.parsing.HotelServiceConverter;
import model.hotelservice.HotelService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class HotelServiceReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(HotelServiceReaderWriter.class);
    @ConfigProperty(propertyName = "csv.services.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeServices(final List<HotelService> hotelServices) {
        try (FileWriter fileWriter = new FileWriter(new File(ClientReaderWriter.class.getClassLoader().
                getResource(fileName).getFile()))) {
            for (HotelService hotelService : hotelServices) {
                fileWriter.write(HotelServiceConverter.getStringFromService(hotelService, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing services: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<HotelService> readServices() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                ClientReaderWriter.class.getClassLoader().getResource(fileName).getFile())))) {
            List<HotelService> hotelServices = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                hotelServices.add(HotelServiceConverter.parseService(line, separator));
            }
            return hotelServices;
        } catch (Exception ex) {
            LOGGER.error("Error at reading services: " + ex.getMessage());
            throw new BusinessException("Could not read services");
        }
    }
}

