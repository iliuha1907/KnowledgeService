package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.hotelService.HotelService;

import java.util.List;

public interface HotelServiceRepository {
    void setHotelServices(List<HotelService> hotelServices);

    List<HotelService> getHotelServices();
}

