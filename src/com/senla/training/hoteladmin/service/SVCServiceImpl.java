package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.repo.SVCRepo;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.svc.Service;
import com.senla.training.hoteladmin.model.svc.ServiceSortCriterion;
import com.senla.training.hoteladmin.model.svc.ServiceType;
import com.senla.training.hoteladmin.util.sort.SVCSorter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SVCServiceImpl implements SVCService {
    private static SVCServiceImpl instance;
    private SVCRepo svcRepo;

    private SVCServiceImpl(SVCRepo svcRepo) {
        this.svcRepo = svcRepo;
    }

    public static SVCServiceImpl getInstance(SVCRepo svcRepo) {
        if (instance == null) {
            instance = new SVCServiceImpl(svcRepo);
            return instance;
        }
        return instance;
    }

    public boolean addService(Service service) {
        if (!(service.getDate().compareTo(service.getClient().getArrivalDate()) > -1 &&
                service.getDate().compareTo(service.getClient().getDepartureDate()) < 1)) {
            return false;
        }
        List<Service> services = svcRepo.getServices();
        services.add(service);
        svcRepo.setServices(services);
        return true;
    }

    public boolean setServicePrice(ServiceType type, BigDecimal price) {
        boolean exists = false;
        List<Service> services = svcRepo.getServices();
        ListIterator iterator = services.listIterator();
        while (iterator.hasNext()) {
            Service service = (Service) iterator.next();
            if (service.getType().equals(type)) {
                service.setPrice(price);
                exists = true;
            }
        }
        svcRepo.setServices(services);
        return exists;
    }

    private List<Service> getClientServices(List<Service> services, Client client) {
        LinkedList<Service> result = new LinkedList<>();
        services.forEach(e -> {
            if (e.getClient().equals(client)) {
                result.add(e);
            }
        });
        return result;
    }

    public List<Service> getSortedClientServices(Client client, ServiceSortCriterion criterion) {
        List<Service> services = getClientServices(svcRepo.getServices(), client);
        switch (criterion) {
            case DATE:
                SVCSorter.sortByDate(services);
                break;
            case PRICE:
                SVCSorter.sortByPrice(services);
                break;
        }
        return services;
    }

    public List<Service> getServices(ServiceSortCriterion criterion) {
        List<Service> services = svcRepo.getServices();
        switch (criterion) {
            case DATE:
                SVCSorter.sortByDate(services);
                break;
            case PRICE:
                SVCSorter.sortByPrice(services);
                break;
        }
        return services;
    }
}

