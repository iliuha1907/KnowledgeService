package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.svc.Service;

import java.util.LinkedList;
import java.util.List;

public class SVCRepoImpl implements SVCRepo{
    private static SVCRepoImpl instance;
    private List<Service> services;

    private SVCRepoImpl() {
        services = new LinkedList<>();
    }

    public static SVCRepoImpl getInstance() {
        if (instance == null) {
            instance = new SVCRepoImpl();
        }
        return instance;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Service> getServices() {
        return services;
    }
}

