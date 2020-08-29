package com.senla.training.hoteladmin.util.filecsv.writeread;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.util.filecsv.parsing.HotelServiceConverter;
import com.senla.training.injection.annotation.ConfigProperty;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

@NeedInjectionClass
public class HotelServiceReaderWriter {
    @ConfigProperty(propertyName = "csv.services.filePath", type = String.class)
    private static String fileName;
    @ConfigProperty(propertyName = "csv.separator", type = String.class)
    private static String separator;

    public static void writeServices(List<HotelService> hotelServices) {
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            for (HotelService hotelService : hotelServices) {
                fileWriter.write(HotelServiceConverter.getStringFromService(hotelService, separator) + "\n");
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static List<HotelService> readServices() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            List<HotelService> hotelServices = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                hotelServices.add(HotelServiceConverter.parseService(line, separator));
            }
            return hotelServices;
        } catch (Exception ex) {
            throw new BusinessException("Could not read services");
        }
    }
}

