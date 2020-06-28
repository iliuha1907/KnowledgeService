package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.svc.HotelService;
import com.senla.training.hoteladmin.util.file.HotelServiceParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HotelServiceWriterImpl implements HotelServiceWriter {
    private static HotelServiceWriterImpl instance;

    private HotelServiceWriterImpl() {
    }

    public static HotelServiceWriter getInstance() {
        if (instance == null) {
            instance = new HotelServiceWriterImpl();
        }
        return instance;
    }

    @Override
    public void writeServices(List<HotelService> hotelServices) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(FILE_NAME));
        for (HotelService hotelService : hotelServices) {
            fileWriter.write(HotelServiceParser.getStringFromService(hotelService, SEPARATOR));
            fileWriter.write(hotelService.getClient().getId() + SEPARATOR);
            fileWriter.write(hotelService.getClient().getRoom().getId() + SEPARATOR + "\n");
        }
        fileWriter.close();
    }

    @Override
    public List<HotelService> readServices() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)));
        List<HotelService> hotelServices = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            hotelServices.add(HotelServiceParser.parseService(line, SEPARATOR));
        }
        reader.close();
        return hotelServices;
    }
}

