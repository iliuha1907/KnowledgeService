package com.senla.training.hoteladmin.dao.hotelservice;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.dao.HibernateAbstractDao;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;

@NeedInjectionClass
public class HotelServiceDaoImpl extends HibernateAbstractDao<HotelService>
        implements HotelServiceDao {

    @Override
    public Class<HotelService> getEntityClass() {
        return HotelService.class;
    }
}
