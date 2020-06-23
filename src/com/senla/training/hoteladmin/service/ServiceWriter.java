package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.svc.Service;

import java.io.IOException;
import java.util.List;

public interface ServiceWriter {
    String FILE_NAME = "Files/services.csv";
    String SEPARATOR = ";";

    void writeServices(List<Service> services) throws IOException;

    List<Service> readServices() throws IOException;
}

