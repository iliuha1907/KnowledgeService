package service;

import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import dao.DaoManager;
import dao.hotelservice.HotelServiceDao;
import exception.BusinessException;
import filecsv.writeread.HotelServiceReaderWriter;
import model.hotelservice.HotelService;
import model.hotelservice.HotelServiceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

@NeedInjectionClass
public class HotelServiceServiceImpl implements HotelServiceService {

    private static final Logger LOGGER = LogManager.getLogger(HotelServiceServiceImpl.class);
    @NeedInjectionField
    private HotelServiceDao hotelServiceDao;
    @NeedInjectionField
    private DaoManager daoManager;

    @Override
    public void addService(final BigDecimal price, final HotelServiceType type) {
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            hotelServiceDao.add(new HotelService(price, type), connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public void setServicePrice(final Integer id, final BigDecimal price) {
        Connection connection = daoManager.getConnection();
        HotelService hotelService = hotelServiceDao.getById(id, connection);
        if (hotelService == null) {
            LOGGER.error("Error at setting service price: No such service");
            throw new BusinessException("No such service");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            hotelServiceDao.updateById(hotelService.getId(), price, "price", connection);
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public List<HotelService> getServices() {
        return hotelServiceDao.getAll(daoManager.getConnection());
    }

    @Override
    public void exportServices() {
        HotelServiceReaderWriter.writeServices(hotelServiceDao.getAll(daoManager.getConnection()));
    }

    @Override
    public void importServices() {
        List<HotelService> hotelServices = HotelServiceReaderWriter.readServices();
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            hotelServices.forEach(hotelService -> {
                HotelService existing = hotelServiceDao.getById(hotelService.getId(), connection);
                if (existing == null) {
                    hotelServiceDao.add(hotelService, connection);
                }
            });
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }
}

