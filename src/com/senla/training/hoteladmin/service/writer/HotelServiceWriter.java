package com.senla.training.hoteladmin.service.writer;

import com.senla.training.hoteladmin.model.hotelService.HotelService;

import java.io.IOException;
import java.util.List;

public interface HotelServiceWriter {
    String FILE_NAME = "Files/services.csv";
    String SEPARATOR = ";";

    void writeServices(List<HotelService> hotelServices) throws IOException;

    List<HotelService> readServices() throws IOException;
}

