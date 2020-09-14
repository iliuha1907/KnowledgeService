package com.senla.training.hoteladmin.service.hotelservice;

import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.csvapi.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.hotelservice.HotelService_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

@Component
@Transactional
public class HotelServiceServiceImpl implements HotelServiceService {

    @Autowired
    private HotelServiceDao hotelServiceDao;
    @Autowired
    private EntityManagerProvider entityManagerProvider;
    @Autowired
    private HotelServiceReaderWriter hotelServiceReaderWriter;

    @Override
    public void addService(BigDecimal price, HotelServiceType type) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        try {
            hotelServiceDao.add(new HotelService(price, type), entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void setServicePrice(Integer id, BigDecimal price) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        try {
            hotelServiceDao.updateByAttribute(id, HotelService_.id, price, HotelService_.price, entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<HotelService> getServices() {
        return hotelServiceDao.getAll(entityManagerProvider.getEntityManager());
    }

    @Override
    public void exportServices() {
        hotelServiceReaderWriter.writeServices(hotelServiceDao.getAll(entityManagerProvider.getEntityManager()));
    }

    @Override
    public void importServices() {
        List<HotelService> hotelServices = hotelServiceReaderWriter.readServices();
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        try {
            hotelServices.forEach(hotelService -> {
                hotelServiceDao.add(hotelService, entityManager);
            });
        } catch (Exception ex) {
            throw ex;
        }
    }
}
