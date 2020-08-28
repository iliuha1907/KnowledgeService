package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.service.ClientService;
import com.senla.training.hoteladmin.service.HotelServiceService;

import java.math.BigDecimal;
import java.util.List;

@NeedInjectionClass
public class HotelServiceController {
    @NeedInjectionField
    private HotelServiceService hotelServiceService;
    @NeedInjectionField
    private ClientService clientService;

    public String addService(BigDecimal price, HotelServiceType type) {
        try {
            hotelServiceService.addService(price, type);
            return "Successfully added service";
        } catch (Exception ex) {
            return "Error at adding service: " + ex.getMessage();
        }
    }

    public String setServicePrice(Integer id, BigDecimal price) {
        try {
            hotelServiceService.setServicePrice(id, price);
            return "Successfully updated service";
        } catch (Exception ex) {
            return "Error at updating service: " + ex.getMessage();
        }
    }

    public String getServices() {
        List<HotelService> hotelServices;
        try {
            hotelServices = hotelServiceService.getServices();
        } catch (Exception ex) {
            return "Error at getting services: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Services:\n");
        hotelServices.forEach(hotelService ->
                result.append(hotelService).append("\n")
        );
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
}

