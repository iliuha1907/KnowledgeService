package com.senla.training.hoteladmin.repository;

import com.senla.training.injection.annotation.InterfaceOfInjection;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;

import java.util.List;

@InterfaceOfInjection
public interface HotelServiceRepository {

    void setHotelServices(List<HotelService> hotelServices);

    void addHotelService(HotelService hotelService);

    void removeClientHotelServices(Integer clientId);

    HotelService getHotelServiceById(Integer id);

    HotelService getHotelServiceByClientId(Integer clientId);

    List<HotelService> getHotelServices();

    List<HotelService> getClientHotelServices(Integer clientId);
}

