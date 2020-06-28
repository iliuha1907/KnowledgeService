package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.svc.HotelService;
import com.senla.training.hoteladmin.util.sort.HotelServiceSortCriterion;
import com.senla.training.hoteladmin.model.svc.HotelServiceType;

import java.math.BigDecimal;
import java.util.List;

public interface HotelServiceService {
    boolean addService(HotelService hotelService);

    boolean setServicePrice(HotelServiceType type, BigDecimal price);

    List<HotelService> getSortedClientServices(Client client, HotelServiceSortCriterion criterion);

    List<HotelService> getServices(HotelServiceSortCriterion criterion);

    HotelService getService(Integer clientId);

    boolean removeService(Integer clientId);

    boolean exportServices();

    boolean importServices(ClientService clientService, RoomService roomService);

    void updateService(HotelService hotelService);
}

