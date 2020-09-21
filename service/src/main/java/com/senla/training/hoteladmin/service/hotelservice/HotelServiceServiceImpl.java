package com.senla.training.hoteladmin.service.hotelservice;

import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.csvapi.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HotelServiceServiceImpl implements HotelServiceService {

    @Autowired
    private HotelServiceDao hotelServiceDao;
    @Autowired
    private HotelServiceReaderWriter hotelServiceReaderWriter;

    @Override
    @Transactional
    public void addService(BigDecimal price, HotelServiceType type) {
        hotelServiceDao.add(new HotelService(price, type));
    }

    @Override
    @Transactional
    public void setServicePrice(Integer id, BigDecimal price) {
        HotelService service = hotelServiceDao.getById(id);
        if (service == null) {
            throw new BusinessException("Error at setting service price: no such service");
        }
        service.setPrice(price);
        hotelServiceDao.update(service);
    }

    @Override
    @Transactional
    public List<HotelService> getServices() {
        return hotelServiceDao.getAll();
    }

    @Override
    @Transactional
    public void exportServices() {
        hotelServiceReaderWriter.writeServices(hotelServiceDao.getAll());
    }

    @Override
    @Transactional
    public void importServices() {
        List<HotelService> hotelServices = hotelServiceReaderWriter.readServices();
        hotelServices.forEach(hotelService -> {
            hotelServiceDao.add(hotelService);
        });
    }
}
