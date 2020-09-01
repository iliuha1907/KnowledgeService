package service;

import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import dao.DaoManager;
import dao.hotelservice.HotelServiceDao;
import dao.client.ClientDao;
import dao.visit.VisitDao;
import exception.BusinessException;
import filecsv.writeread.VisitReaderWriter;
import model.client.Client;
import model.hotelservice.HotelService;
import model.visit.Visit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.sort.VisitSortCriterion;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class VisitServiceImpl implements VisitService {

    private static final Logger LOGGER = LogManager.getLogger(VisitServiceImpl.class);
    @NeedInjectionField
    private VisitDao visitDao;
    @NeedInjectionField
    private HotelServiceDao hotelServiceDao;
    @NeedInjectionField
    private ClientDao clientDao;
    @NeedInjectionField
    private DaoManager daoManager;

    @Override
    public void addVisit(final Integer serviceId, final Integer clientId, final Date date) {
        Connection connection = daoManager.getConnection();
        Client client = clientDao.getById(clientId, connection);
        if (client == null) {
            LOGGER.error("Error at adding visit: No such client");
            throw new BusinessException("No such client");
        }

        HotelService hotelService = hotelServiceDao.getById(serviceId, connection);
        if (hotelService == null) {
            LOGGER.error("Error at adding visit: No such hotel service");
            throw new BusinessException("No such hotel service");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            visitDao.add(new Visit(client, hotelService, date, true), daoManager.getConnection());
            daoManager.connectionCommit();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        } finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public List<Visit> getSortedClientVisits(final Integer clientId, final VisitSortCriterion criterion) {
        return visitDao.getSortedClientVisits(clientId, criterion, daoManager.getConnection());
    }

    @Override
    public void exportVisits() {
        VisitReaderWriter.writeVisits(visitDao.getAll(daoManager.getConnection()));
    }

    @Override
    public void importVisits() {
        List<Visit> visits = VisitReaderWriter.readVisits();
        Connection connection = daoManager.getConnection();
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            visits.forEach(visit -> {
                Client client = clientDao.getById(visit.getClient().getId(), connection);
                HotelService hotelService = hotelServiceDao.getById(visit.getHotelService().getId(),
                        connection);
                if (client != null && hotelService != null) {
                    visitDao.add(visit, daoManager.getConnection());
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

