package com.senla.training.hoteladmin.dao.hotelservice;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import org.springframework.stereotype.Component;

@Component
public class HotelServiceDaoImpl extends AbstractDao<HotelService, Integer>
        implements HotelServiceDao {

    @Override
    public Class<HotelService> getEntityClass() {
        return HotelService.class;
    }
}
