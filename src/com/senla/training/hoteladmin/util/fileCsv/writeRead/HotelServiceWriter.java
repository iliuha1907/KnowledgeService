package com.senla.training.hotelAdmin.util.fileCsv.writeRead;

import com.senla.training.hotelAdmin.model.hotelService.HotelService;
import com.senla.training.hotelAdmin.util.fileCsv.parsing.HotelServiceParser;
import com.senla.training.hotelAdmin.util.fileProperties.PropertyDataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HotelServiceWriter {
    private static final String FILE_NAME = PropertyDataProvider.getServicesCsv();
    private static final String SEPARATOR = PropertyDataProvider.getSeparator();

    public static boolean writeServices(List<HotelService> hotelServices) {
        try (FileWriter fileWriter = new FileWriter(new File(FILE_NAME))) {
            for (HotelService hotelService : hotelServices) {
                fileWriter.write(HotelServiceParser.getStringFromService(hotelService, SEPARATOR));
                fileWriter.write(hotelService.getClient().getId() + SEPARATOR + "\n");
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static List<HotelService> readServices() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)))) {
            List<HotelService> hotelServices = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                hotelServices.add(HotelServiceParser.parseService(line, SEPARATOR));
            }
            return hotelServices;
        } catch (Exception ex) {
            return null;
        }
    }
}

