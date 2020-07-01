package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelService.HotelService;
import com.senla.training.hoteladmin.model.hotelService.HotelServiceType;
import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.service.HotelServiceService;
import com.senla.training.hoteladmin.util.HotelServiceIdProvider;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HotelServiceController {
    private static HotelServiceController instance;
    private HotelServiceService hotelServiceService;
    private ClientService clientService;

    private HotelServiceController(HotelServiceService hotelServiceService, ClientService clientService) {
        this.hotelServiceService = hotelServiceService;
        this.clientService = clientService;
    }

    public static HotelServiceController getInstance(HotelServiceService hotelServiceService, ClientService clientService){
        if(instance == null){
            instance = new HotelServiceController(hotelServiceService, clientService);
        }
        return instance;
    }

    public String addService(BigDecimal price, HotelServiceType type, Integer clientPass, Date date) {
        Client client = clientService.getClientById(clientPass);
        if (client == null) {
            return "Error at adding hotelService: no such client!";
        }
        Integer startId = HotelServiceIdProvider.getId();
        HotelService hotelService = new HotelService(HotelServiceIdProvider.getNextId(), price, type, client, date);
        while (hotelServiceService.getServiceById(hotelService.getId()) != null) {
            hotelService.setId(HotelServiceIdProvider.getNextId());
        }

        if (!hotelServiceService.addService(hotelService)) {
            HotelServiceIdProvider.setId(startId);
            return "Error at adding hotelService: incompatible dates!";
        } else {
            return "Successfully added hotelService";
        }
    }

    public String setServicePrice(HotelServiceType type, BigDecimal price) {
        if (hotelServiceService.setServicePrice(type, price)) {
            return "Successfully modified price";
        } else {
            return "Error at updating price of the services: no such type!";
        }
    }

    public String getSortedClientServices(Integer passportNumber, HotelServiceSortCriterion criterion) {
        Client client = clientService.getClientById(passportNumber);
        if (client == null) {
            return "Error at getting hotelServices of a client: no such client!";
        }
        List<HotelService> hotelServices = hotelServiceService.getSortedClientServices(client, criterion);
        StringBuilder result = new StringBuilder("Services:\n");
        hotelServices.forEach(e -> {
            result.append(e).append("\n");
        });
        return result.toString();
    }

    public String getServices() {
        List<HotelService> hotelServices = hotelServiceService.getServices(HotelServiceSortCriterion.PRICE);
        StringBuilder result = new StringBuilder("Services:\n");
        hotelServices.forEach(e -> {
            result.append(e).append("\n");
        });
        return result.toString();
    }

    public String exportServices(){
        if(hotelServiceService.exportServices()){
            return "Successfully exported services";
        }
        else {
            return "Could not export services";
        }
    }

    public String importServices(){
        if(hotelServiceService.importServices()){
            return "Successfully imported services";
        }
        else {
            return "Could not import services";
        }
    }
}

