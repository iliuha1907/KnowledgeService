package com.senla.training.hoteladmin.service.hotelservice;

import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;

import java.math.BigDecimal;
import java.util.List;

public interface HotelServiceService {

    void addService(BigDecimal price, HotelServiceType type);

    void updateService(HotelService service, Integer id);

    List<HotelService> getServices();

    void exportServices();

    void importServices();
}
