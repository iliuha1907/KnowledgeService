package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.svc.Service;
import com.senla.training.hoteladmin.util.file.ClientParser;
import com.senla.training.hoteladmin.util.file.RoomParser;
import com.senla.training.hoteladmin.util.file.ServiceParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceWriterImpl implements ServiceWriter {
    private static ServiceWriterImpl instance;

    private ServiceWriterImpl() {
    }

    public static ServiceWriter getInstance() {
        if (instance == null) {
            instance = new ServiceWriterImpl();
        }
        return instance;
    }

    @Override
    public void writeServices(List<Service> services) throws IOException {
        FileWriter fileWriter = new FileWriter(new File(FILE_NAME));
        for (Service service : services) {
            fileWriter.write(ServiceParser.getStringFromService(service, SEPARATOR));
            fileWriter.write(service.getClient().getId() + SEPARATOR);
            fileWriter.write(service.getClient().getRoom().getId() + SEPARATOR + "\n");
        }
        fileWriter.close();
    }

    @Override
    public List<Service> readServices() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(FILE_NAME)));
        List<Service> services = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            services.add(ServiceParser.parseService(line, SEPARATOR));
        }
        reader.close();
        return services;
    }
}

