package com.senla.training.hoteladmin.service.visit;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {

    private static final Logger LOGGER = LogManager.getLogger(VisitServiceImpl.class);
    @Autowired
    private VisitDao visitDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private HotelServiceDao hotelServiceDao;
    @Autowired
    private VisitReaderWriter visitReaderWriter;

    @Override
    @Transactional
    public void addVisit(Integer serviceId, Integer clientId, Date date) {
        Client client = clientDao.getByPrimaryKey(clientId);
        if (client == null) {
            LOGGER.error("Error at adding visit: No such client");
            throw new BusinessException("Error at adding visit: No such client");
        }

        HotelService hotelService = hotelServiceDao.getByPrimaryKey(serviceId);
        if (hotelService == null) {
            LOGGER.error("Error at adding visit: No such hotel service");
            throw new BusinessException("Error at adding visit: No such hotel service");
        }

        visitDao.add(new Visit(client, hotelService, java.sql.Date.valueOf(DateUtil.getString(date)), true));
    }

    @Override
    @Transactional
    public List<Visit> getSortedClientVisits(Integer clientId, VisitSortCriterion criterion) {
        Client client = clientDao.getByPrimaryKey(clientId);
        if (client == null) {
            LOGGER.error("Error at getting visits: No such client");
            throw new BusinessException("Error at getting visits: No such client");
        }
        return visitDao.getSortedClientVisits(client, criterion);
    }

    @Override
    @Transactional
    public void exportVisits() {
        visitReaderWriter.writeVisits(visitDao.getAll());
    }

    @Override
    @Transactional
    public void importVisits() {
        List<Visit> visits = visitReaderWriter.readVisits();
        visits.forEach(visit -> {
            Client client = clientDao.getByPrimaryKey(visit.getClient().getId());
            HotelService hotelService = hotelServiceDao.getByPrimaryKey(visit.getService().getId());
            if (client != null && hotelService != null) {
                visit.setClient(client);
                visit.setService(hotelService);
                visitDao.add(visit);
            }
        });
    }
}
