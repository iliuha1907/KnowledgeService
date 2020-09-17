package com.senla.training.hoteladmin.csvapi.writeread;

import com.senla.training.hoteladmin.csvapi.parsing.HotelServiceConverter;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
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
public class HotelServiceReaderWriter {

    private static final Logger LOGGER = LogManager.getLogger(HotelServiceReaderWriter.class);
    @Value("${csv.services.import.filePath:csv/services.csv}")
    private String fileNameImport;
    @Value("${csv.services.export.filePath:exportCsv/services.csv}")
    private String fileNameExport;
    @Value("${csv.separator:;}")
    private String separator;

    public void writeServices(final List<HotelService> hotelServices) {
        try (FileWriter fileWriter = new FileWriter(new File(fileNameExport))) {
            for (HotelService hotelService : hotelServices) {
                fileWriter.write(HotelServiceConverter.getStringFromService(hotelService, separator) + "\n");
            }
        } catch (Exception ex) {
            LOGGER.error("Error at writing services: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    public List<HotelService> readServices() {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(
                ClientReaderWriter.class.getClassLoader().getResource(fileNameImport).getFile())))) {
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

