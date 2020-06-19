package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.svc.Service;
import com.senla.training.hoteladmin.model.svc.ServiceSortCriterion;
import com.senla.training.hoteladmin.model.svc.ServiceType;

import java.math.BigDecimal;
import java.util.List;

public interface SVCService {
    boolean addService(Service service);

    boolean setServicePrice(ServiceType type, BigDecimal price);

    List<Service> getSortedClientServices(Client client, ServiceSortCriterion criterion);

    List<Service> getServices(ServiceSortCriterion criterion);
}

