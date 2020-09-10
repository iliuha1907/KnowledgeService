package com.senla.training.hoteladmin.service.hotelserice;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionField;
import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.csvapi.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceField;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;

@NeedInjectionClass
public class HotelServiceServiceImpl implements HotelServiceService {

    @NeedInjectionField
    private HotelServiceDao hotelServiceDao;

    @Override
    public void addService(BigDecimal price, HotelServiceType type) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            hotelServiceDao.add(new HotelService(price, type), entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public void setServicePrice(Integer id, BigDecimal price) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            hotelServiceDao.updateById(id, price, HotelServiceField.PRICE.toString().toLowerCase(), entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public List<HotelService> getServices() {
        return hotelServiceDao.getAll(EntityManagerProvider.getEntityManager());
    }

    @Override
    public void exportServices() {
        HotelServiceReaderWriter.writeServices(hotelServiceDao.getAll(EntityManagerProvider.getEntityManager()));
    }

    @Override
    public void importServices() {
        List<HotelService> hotelServices = HotelServiceReaderWriter.readServices();
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            hotelServices.forEach(hotelService -> {
                hotelServiceDao.add(hotelService, entityManager);
            });
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }
}
