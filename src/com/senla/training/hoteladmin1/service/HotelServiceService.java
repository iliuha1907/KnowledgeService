package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface HotelServiceService {

    void setServices(List<HotelService> hotelServices);

    void addService(BigDecimal price, HotelServiceType type, Integer clientId, Date date);

    void setServicePrice(Integer id, BigDecimal price);

    List<HotelService> getSortedClientServices(Client client, HotelServiceSortCriterion criterion);

    List<HotelService> getServices(HotelServiceSortCriterion criterion);

    void exportServices();

    void importServices();

    void updateService(HotelService hotelService);

    void serializeServices();

    void deserializeServices();

    void serializeId();

    void deserializeId();
}

