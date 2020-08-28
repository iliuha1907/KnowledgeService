package com.senla.training.hoteladmin.service;

import com.senla.training.hoteladmin.dao.DaoManager;
import com.senla.training.hoteladmin.dao.clientdao.ClientDao;
import com.senla.training.hoteladmin.dao.hotelservicedao.HotelServiceDao;
import com.senla.training.hoteladmin.dao.visitdao.VisitDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.filecsv.writeread.VisitReaderWriter;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class VisitServiceImpl implements VisitService {
    @NeedInjectionField
    private VisitDao visitDao;
    @NeedInjectionField
    private HotelServiceDao hotelServiceDao;
    @NeedInjectionField
    private ClientDao clientDao;
    @NeedInjectionField
    private DaoManager daoManager;

    @Override
    public void addVisit(Integer serviceId, Integer clientId, Date date) {
        Connection connection = daoManager.getConnection();
        Client client = clientDao.getById(clientId, connection);
        if (client == null) {
            throw new BusinessException("No such client");
        }

        HotelService hotelService = hotelServiceDao.getById(serviceId, connection);
        if (hotelService == null) {
            throw new BusinessException("No such hotel service");
        }

        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            visitDao.add(new Visit(client, hotelService, date, true), daoManager.getConnection());
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        }
        finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }

    @Override
    public List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion) {
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
        visits.forEach(visit -> {
            Client client = clientDao.getById(visit.getClient().getId(), connection);
            HotelService hotelService = hotelServiceDao.getById(visit.getHotelService().getId(), connection);
            if (client != null && hotelService != null) {
                addVisit(new Visit(client,hotelService,visit.getDate(),visit.isActive()));
            }
        });
    }

    private void addVisit(Visit visit) {
        boolean isAutocommit = daoManager.getAutoConnectionCommit();
        daoManager.setConnectionAutocommit(false);
        try {
            visitDao.add(visit, daoManager.getConnection());
            daoManager.commitConnection();
        } catch (Exception ex) {
            daoManager.rollbackConnection();
            throw ex;
        }
        finally {
            daoManager.setConnectionAutocommit(isAutocommit);
        }
    }
}

