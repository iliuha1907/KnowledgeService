package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.repo.SvcRepo;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.svc.Service;
import com.senla.training.hoteladmin.util.ServiceIdProvider;
import com.senla.training.hoteladmin.util.file.RoomFileHelper;
import com.senla.training.hoteladmin.util.file.ServiceFileHelper;
import com.senla.training.hoteladmin.util.sort.ServiceSortCriterion;
import com.senla.training.hoteladmin.model.svc.ServiceType;
import com.senla.training.hoteladmin.util.sort.SvcSorter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SvcServiceImpl implements SvcService {
    private static SvcServiceImpl instance;
    private SvcRepo svcRepo;

    private SvcServiceImpl(SvcRepo svcRepo) {
        this.svcRepo = svcRepo;
    }

    public static SvcService getInstance(SvcRepo svcRepo) {
        if (instance == null) {
            instance = new SvcServiceImpl(svcRepo);
            return instance;
        }
        return instance;
    }

    @Override
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

    @Override
    public boolean setServicePrice(ServiceType type, BigDecimal price) {
        boolean exists = false;
        List<Service> services = svcRepo.getServices();
        ListIterator<Service> iterator = services.listIterator();
        while (iterator.hasNext()) {
            Service service = iterator.next();
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

    @Override
    public List<Service> getSortedClientServices(Client client, ServiceSortCriterion criterion) {
        List<Service> services = getClientServices(svcRepo.getServices(), client);
        if(criterion.equals(ServiceSortCriterion.DATE)){
            SvcSorter.sortByDate(services);
        }
        else if(criterion.equals(ServiceSortCriterion.PRICE)){
            SvcSorter.sortByPrice(services);
        }
        return services;
    }

    @Override
    public List<Service> getServices(ServiceSortCriterion criterion) {
        List<Service> services = svcRepo.getServices();
        if(criterion.equals(ServiceSortCriterion.DATE)){
            SvcSorter.sortByDate(services);
        }
        else if(criterion.equals(ServiceSortCriterion.PRICE)){
            SvcSorter.sortByPrice(services);
        }
        return services;
    }

    @Override
    public boolean exportServices() {
        try {
            ServiceFileHelper.writeServices(svcRepo.getServices());
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean importServices(ClientService clientService, RoomService roomService) {
        List<Service> services;
        try {
            services = ServiceFileHelper.readServices();
            services.forEach(e->{
                updateService(e);
                clientService.updateClient(e.getClient());
                roomService.updateRoom(e.getClient().getRoom());
            });
        }
        catch (Exception ex){
            return false;
        }

        return true;
    }

    @Override
    public void updateService(Service service) {
        if(service == null){
            return;
        }
        List<Service> services = svcRepo.getServices();
        int index = services.indexOf(service);
        if(index == -1){
            service.setId(ServiceIdProvider.getNextId());
            services.add(service);
        }
        else {
            services.set(index,service);
        }
        svcRepo.setServices(services);
    }
}

