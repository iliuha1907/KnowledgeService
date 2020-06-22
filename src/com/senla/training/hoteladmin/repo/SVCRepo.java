package com.senla.training.hoteladmin.repo;

import com.senla.training.hoteladmin.model.svc.Service;

import java.util.List;

public interface SvcRepo {
    void setServices(List<Service> services);

    List<Service> getServices();
}

