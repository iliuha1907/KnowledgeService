package com.senla.training.hoteladmin.service.visit;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionField;
import com.senla.training.hoteladmin.dao.EntityManagerProvider;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dao.visit.VisitDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.csvapi.writeread.VisitReaderWriter;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class VisitServiceImpl implements VisitService {

    private static final Logger LOGGER = LogManager.getLogger(VisitServiceImpl.class);
    @NeedInjectionField
    private VisitDao visitDao;
    @NeedInjectionField
    private ClientDao clientDao;
    @NeedInjectionField
    private HotelServiceDao hotelServiceDao;

    @Override
    public void addVisit(Integer serviceId, Integer clientId, Date date) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        Client client = clientDao.getById(clientId, entityManager);
        if (client == null) {
            LOGGER.error("Error at adding visit: No such client");
            throw new BusinessException("No such client");
        }

        HotelService hotelService = hotelServiceDao.getById(serviceId, entityManager);
        if (hotelService == null) {
            LOGGER.error("Error at adding visit: No such hotel service");
            throw new BusinessException("No such hotel service");
        }

        transaction.begin();
        try {
            visitDao.add(new Visit(client, hotelService, java.sql.Date.valueOf(DateUtil.getString(date)), true),
                    entityManager);
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }

    @Override
    public List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion) {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        Client client = clientDao.getById(clientId, entityManager);
        if (client == null) {
            LOGGER.error("Error at getting visits: No such client");
            throw new BusinessException("No such client");
        }
        return visitDao.getSortedClientVisits(client, criterion, entityManager);
    }

    @Override
    public void exportVisits() {
        VisitReaderWriter.writeVisits(visitDao.getAll(EntityManagerProvider.getEntityManager()));
    }

    @Override
    public void importVisits() {
        EntityManager entityManager = EntityManagerProvider.getEntityManager();
        List<Visit> visits = VisitReaderWriter.readVisits();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            visits.forEach(visit -> {
                Client client = clientDao.getById(visit.getClient().getId(), entityManager);
                HotelService hotelService = hotelServiceDao.getById(visit.getService().getId(),
                        entityManager);
                if (client != null && hotelService != null) {
                    visit.setClient(client);
                    visit.setService(hotelService);
                    visitDao.add(visit, entityManager);
                }
            });
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
        transaction.commit();
    }
}
