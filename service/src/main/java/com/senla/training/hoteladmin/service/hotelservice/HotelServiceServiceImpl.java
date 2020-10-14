package com.senla.training.hoteladmin.service.hotelservice;

import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.csvapi.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HotelServiceServiceImpl implements HotelServiceService {

    private static final Logger LOGGER = LogManager.getLogger(HotelServiceService.class);
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
    public void updateService(HotelService service, Integer id) {
        if (service == null) {
            LOGGER.error("Error at updating Service: Service is null");
            throw new BusinessException("Error at updating Service: Service is null");
        }
        service.setId(id);
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
