package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.service.SvcService;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.svc.Service;
import com.senla.training.hoteladmin.util.sort.ServiceSortCriterion;
import com.senla.training.hoteladmin.model.svc.ServiceType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SvcController {
    private static SvcController instance;
    private SvcService svcService;
    private ClientService clientService;

    private SvcController(SvcService svcService, ClientService clientService) {
        this.svcService = svcService;
        this.clientService = clientService;
    }

    public static SvcController getInstance(SvcService svcService, ClientService clientService){
        if(instance == null){
            instance = new SvcController(svcService, clientService);
        }
        return instance;
    }

    public String addService(BigDecimal price, ServiceType type, Integer clientPass, Date date) {
        Client client = clientService.getClientByPass(clientPass);
        if (client == null) {
            return "Error at adding service: no such client!";
        }
        Service service = new Service(price, type, client, date);
        if (!svcService.addService(service)) {
            return "Error at adding service: incompatible dates!";
        } else {
            return "Successfully added service";
        }
    }

    public String setServicePrice(ServiceType type, BigDecimal price) {
        if (svcService.setServicePrice(type, price)) {
            return "Successfully modified price";
        } else {
            return "Error at updating price of the services: no such type!";
        }
    }

    public String getSortedClientServices(Integer passportNumber, ServiceSortCriterion criterion) {
        Client client = clientService.getClientByPass(passportNumber);
        if (client == null) {
            return "Error at getting services of a client: no such client!";
        }
        List<Service> services = svcService.getSortedClientServices(client, criterion);
        StringBuilder result = new StringBuilder("Services:\n");
        services.forEach(e -> {
            String part = e + "\n";
            result.append(part);
        });
        return result.toString();
    }

    public String getServices() {
        List<Service> services = svcService.getServices(ServiceSortCriterion.PRICE);
        StringBuilder result = new StringBuilder("Services:\n");
        services.forEach(e -> {
            String part = e + "\n";
            result.append(part);
        });
        return result.toString();
    }
}

