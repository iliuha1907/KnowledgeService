package com.senla.training.hoteladmin.repository;

import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.util.idspread.HotelServiceIdProvider;
import com.senla.training.injection.annotation.NeedInjectionClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NeedInjectionClass
public class HotelServiceRepositoryImpl implements HotelServiceRepository {
    private List<HotelService> hotelServices;

    public HotelServiceRepositoryImpl() {
        hotelServices = new ArrayList<>();
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

    @Override
    public void removeClientHotelServices(Integer clientId) {
        HotelService hotelService;
        while ((hotelService = getHotelServiceByClientId(clientId)) != null) {
            hotelServices.remove(hotelService);
        }
    }

    @Override
    public HotelService getHotelServiceById(Integer id) {
        try {
            return hotelServices.stream()
                    .filter(hotelService -> hotelService.getId().equals(id))
                    .findFirst()
                    .get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public HotelService getHotelServiceByClientId(Integer clientId) {
        try {
            return hotelServices.stream()
                    .filter(hotelService -> hotelService.getClient().getId().equals(clientId))
                    .findFirst()
                    .get();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<HotelService> getHotelServices() {
        return hotelServices;
    }

    @Override
    public List<HotelService> getClientHotelServices(Integer clientId) {
        return hotelServices.stream()
                .filter(hotelService -> hotelService.getClient().getId().equals(clientId))
                .collect(Collectors.toList());
    }
}

