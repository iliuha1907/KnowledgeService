package com.senla.training.hoteladmin.service.visit;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class VisitServiceImpl implements VisitService {

    private static final Logger LOGGER = LogManager.getLogger(VisitServiceImpl.class);
    @Autowired
    private VisitDao visitDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private HotelServiceDao hotelServiceDao;
    @Autowired
    private EntityManagerProvider entityManagerProvider;
    @Autowired
    private VisitReaderWriter visitReaderWriter;

    @Override
    public void addVisit(Integer serviceId, Integer clientId, Date date) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
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

        try {
            visitDao.add(new Visit(client, hotelService, java.sql.Date.valueOf(DateUtil.getString(date)), true),
                    entityManager);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion) {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Client client = clientDao.getById(clientId, entityManager);
        if (client == null) {
            LOGGER.error("Error at getting visits: No such client");
            throw new BusinessException("No such client");
        }
        return visitDao.getSortedClientVisits(client, criterion, entityManager);
    }

    @Override
    public void exportVisits() {
        visitReaderWriter.writeVisits(visitDao.getAll(entityManagerProvider.getEntityManager()));
    }

    @Override
    public void importVisits() {
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        List<Visit> visits = visitReaderWriter.readVisits();
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
            throw ex;
        }
    }
}
