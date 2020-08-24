package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.dao.DaoManager;
import com.senla.training.hoteladmin.dao.hotelservicedao.HotelServiceDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

@NeedInjectionClass
public class HotelServiceServiceImpl implements HotelServiceService {
    @NeedInjectionField
    private HotelServiceDao hotelServiceDao;
    @NeedInjectionField
    private DaoManager daoManager;

    @Override
    public void addService(BigDecimal price, HotelServiceType type) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            hotelServiceDao.add(new HotelService(price, type), connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            daoManager.setConnectionAutocommit(isAutocommit);
            throw ex;
        }
        daoManager.setConnectionAutocommit(isAutocommit);
    }

    @Override
    public void setServicePrice(Integer id, BigDecimal price) {
        Connection connection = daoManager.getConnection();
        HotelService hotelService = hotelServiceDao.getById(id, connection);
        if (hotelService == null) {
            throw new BusinessException("No such service");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            hotelServiceDao.updateById(hotelService.getId(), price, "price", connection);
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            daoManager.setConnectionAutocommit(isAutocommit);
            throw ex;
        }
        daoManager.setConnectionAutocommit(isAutocommit);
    }

    @Override
    public List<HotelService> getServices() {
        return hotelServiceDao.getAll(daoManager.getConnection());
    }
}

