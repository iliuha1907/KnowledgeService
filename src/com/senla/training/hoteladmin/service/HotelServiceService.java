package com.senla.training.hotelAdmin.service;

import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.model.hotelService.HotelService;
import com.senla.training.hotelAdmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hotelAdmin.model.hotelService.HotelServiceType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface HotelServiceService {

    void addService(BigDecimal price, HotelServiceType type, Integer clientId, Date date);

    void setServicePrice(Integer id, BigDecimal price);

    List<HotelService> getSortedClientServices(Client client, HotelServiceSortCriterion criterion);

    List<HotelService> getServices(HotelServiceSortCriterion criterion);

    void exportServices();

    void importServices();

    void updateService(HotelService hotelService);
}

