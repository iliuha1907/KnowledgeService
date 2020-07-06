package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.service.ClientServiceImpl;
import com.senla.training.hoteladmin.service.HotelServiceService;
import com.senla.training.hoteladmin.service.HotelServiceServiceImpl;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class HotelServiceController {
    private static HotelServiceController instance;
    private HotelServiceService hotelServiceService;
    private ClientService clientService;

    private HotelServiceController() {
        this.hotelServiceService = HotelServiceServiceImpl.getInstance();
        this.clientService = ClientServiceImpl.getInstance();
    }

    public static HotelServiceController getInstance() {
        if (instance == null) {
            instance = new HotelServiceController();
        }
        return instance;
    }

    public String addService(BigDecimal price, HotelServiceType type, Integer clientId, Date date) {
        try {
            hotelServiceService.addService(price, type, clientId, date);
            return "Successfully added service";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String setServicePrice(Integer id, BigDecimal price) {
        try {
            hotelServiceService.setServicePrice(id, price);
            return "Successfully modified price";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String getSortedClientServices(Integer passportNumber, HotelServiceSortCriterion criterion) {
        Client client = clientService.getClientById(passportNumber);
        if (client == null) {
            return "Error at getting hotelServices of a client: no such client!";
        }
        List<HotelService> hotelServices = hotelServiceService.getSortedClientServices(client, criterion);
        StringBuilder result = new StringBuilder("Services:\n");
        hotelServices.forEach(hotelService -> {
            result.append(hotelService).append("\n");
        });
        return result.toString();
    }

    public String getServices() {
        List<HotelService> hotelServices = hotelServiceService.getServices(HotelServiceSortCriterion.PRICE);
        StringBuilder result = new StringBuilder("Services:\n");
        hotelServices.forEach(hotelService -> {
            result.append(hotelService).append("\n");
        });
        return result.toString();
    }

    public String exportServices() {
        try {
            hotelServiceService.exportServices();
            return "Successfully exported services";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String importServices() {
        try {
            hotelServiceService.importServices();
            return "Successfully imported services";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String  deserializeHotelServices(){
        try {
            hotelServiceService.deserializeServices();
            return  "Successful deserialization of services";
        }
        catch (BusinessException ex){
            return ex.getMessage();
        }
    }

    public String serializeHotelServices(){
        try {
            hotelServiceService.serializeServices();
            return  "Successful serialization of services";
        }
        catch (BusinessException ex){
            return ex.getMessage();
        }
    }

    public String deserializeServicesId() {
        try {
            hotelServiceService.deserializeId();
            return "Successful deserialization of services id";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String serializeServicesId() {
        try {
            hotelServiceService.serializeId();
            return "Successful serialization of services id";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }
}

