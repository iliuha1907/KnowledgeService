package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.svc.Service;

import java.util.LinkedList;
import java.util.List;

public class SvcRepoImpl implements SvcRepo {
    private static SvcRepoImpl instance;
    private List<Service> services;

    private SvcRepoImpl() {
        services = new LinkedList<>();
    }

    public static SvcRepo getInstance() {
        if (instance == null) {
            instance = new SvcRepoImpl();
        }
        return instance;
    }

    @Override
    public void setServices(List<Service> services) {
        this.services = services;
    }

    @Override
    public List<Service> getServices() {
        return services;
    }
}

