package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.hotelService.HotelService;

import java.util.ArrayList;
import java.util.List;

public class HotelServiceRepositoryImpl implements HotelServiceRepository {
    private static HotelServiceRepositoryImpl instance;
    private List<HotelService> hotelServices;

    private HotelServiceRepositoryImpl() {
        hotelServices = new ArrayList<>();
    }

    public static HotelServiceRepository getInstance() {
        if (instance == null) {
            instance = new HotelServiceRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void setHotelServices(List<HotelService> hotelServices) {
        this.hotelServices = hotelServices;
    }

    @Override
    public List<HotelService> getHotelServices() {
        return hotelServices;
    }
}

