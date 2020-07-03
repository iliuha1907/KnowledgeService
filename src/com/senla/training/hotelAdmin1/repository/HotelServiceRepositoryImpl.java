package com.senla.training.hotelAdmin.repository;

import com.senla.training.hotelAdmin.model.hotelService.HotelService;
import com.senla.training.hotelAdmin.util.HotelServiceIdProvider;

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
    public void addHotelService(HotelService hotelService) {
        hotelService.setId(HotelServiceIdProvider.getNextId());
        hotelServices.add(hotelService);
    }

    //Здесь while, так как надо удалить все сервисы клинта
    //и пока они есть - удаляю по одному
    @Override
    public void removeClientHotelServices(Integer clientId) {
        HotelService hotelService;
        while ((hotelService = getHotelServiceByClientId(clientId)) != null) {
            hotelServices.remove(hotelService);
        }
    }

    @Override
    public HotelService getHotelServiceById(Integer id) {
        for (HotelService hotelService : hotelServices) {
            if (hotelService.getId().equals(id)) {
                return hotelService;
            }
        }
        return null;
    }

    @Override
    public HotelService getHotelServiceByClientId(Integer clientId) {
        for (HotelService hotelService : hotelServices) {
            if (hotelService.getClient().getId().equals(clientId)) {
                return hotelService;
            }
        }
        return null;
    }

    @Override
    public List<HotelService> getHotelServices() {
        return hotelServices;
    }

    @Override
    public List<HotelService> getClientHotelServices(Integer clientId) {
        List<HotelService> resultHotelServices = new ArrayList<>();
        hotelServices.forEach(hotelService -> {
            if (hotelService.getClient().getId().equals(clientId)) ;
            resultHotelServices.add(hotelService);
        });
        return resultHotelServices;
    }
}

