package service;

import model.hotelservice.HotelService;
import model.hotelservice.HotelServiceType;

import java.math.BigDecimal;
import java.util.List;

public interface HotelServiceService {

    void addService(BigDecimal price, HotelServiceType type);

    void setServicePrice(Integer id, BigDecimal price);

    List<HotelService> getServices();

    void exportServices();

    void importServices();
}

