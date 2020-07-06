package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.util.filecsv.parsing.HotelServiceConverter;
import com.senla.training.hoteladmin.util.fileproperties.PropertyDataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HotelServiceReaderWriter {
    private static final String FILE_NAME = PropertyDataProvider.getServicesCsv();
    private static final String SEPARATOR = PropertyDataProvider.getSeparator();

    public static void writeServices(List<HotelService> hotelServices) {
        try (FileWriter fileWriter = new FileWriter(new File(FILE_NAME))) {
            for (HotelService hotelService : hotelServices) {
                fileWriter.write(HotelServiceConverter.getStringFromService(hotelService, SEPARATOR) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<HotelService> readServices() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)))) {
            List<HotelService> hotelServices = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                hotelServices.add(HotelServiceConverter.parseService(line, SEPARATOR));
            }
            return hotelServices;
        } catch (Exception ex) {
            throw new BusinessException("Could not read services");
        }
    }
}

