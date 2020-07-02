package com.senla.training.hotelAdmin.repository;

import com.senla.training.hotelAdmin.model.hotelService.HotelService;

import java.util.List;

public interface HotelServiceRepository {
    void addHotelService(HotelService hotelService);

    void removeClientHotelServices(Integer clientId);

    HotelService getHotelServiceById(Integer id);

    HotelService getHotelServiceByClientId(Integer clientId);

    List<HotelService> getHotelServices();

    List<HotelService> getClientHotelServices(Integer clientId);
}

