package com.senla.training.hoteladmin.unit;

import com.senla.training.hoteladmin.csvapi.writeread.VisitReaderWriter;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dao.visit.VisitDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.service.visit.VisitServiceImpl;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;
import config.TestConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigurator.class)
class VisitServiceImplTest {

    private static List<Visit> visits;
    @Autowired
    private VisitServiceImpl visitService;
    @Autowired
    private VisitDao visitDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private HotelServiceDao hotelServiceDao;
    @Autowired
    private VisitReaderWriter visitReaderWriter;

    @BeforeAll
    public static void setUp() {
        Client clientMike = new Client(1, "Mike", "Johnson");
        Client clientJohn = new Client(2, "John", "Robertson");
        HotelService hotelServiceMassage = new HotelService(1, BigDecimal.TEN, HotelServiceType.MASSAGE);
        HotelService hotelServiceSpa = new HotelService(2, BigDecimal.TEN, HotelServiceType.SPA);
        Visit visitOne = new Visit(clientMike, hotelServiceMassage);
        Visit visitTwo = new Visit(clientJohn, hotelServiceSpa);
        visits = Arrays.asList(visitOne, visitTwo);
    }

    @Test
    void VisitServiceImpl_addVisit_BusinessException_noClient() {
        String message = "Error at adding visit: No such client";
        Mockito.doReturn(null).when(clientDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.addVisit(0, 0, null));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_addVisit_BusinessException_noService() {
        String message = "Error at adding visit: No such hotel service";
        Mockito.doReturn(null).when(hotelServiceDao).getByPrimaryKey(0);
        Mockito.doReturn(visits.get(0).getClient()).when(clientDao).getByPrimaryKey(1);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.addVisit(0, 1, null));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_getSortedClientVisits() {
        Client client = visits.get(0).getClient();
        VisitSortCriterion criterion = VisitSortCriterion.PRICE;

        Mockito.doReturn(client).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(visits).when(visitDao).getSortedClientVisits(client, VisitSortCriterion.PRICE);

        Assertions.assertIterableEquals(visits, visitService.getSortedClientVisits(1, criterion));
    }

    @Test
    void VisitServiceImpl_getSortedClientVisits_BusinessException() {
        String message = "Error at getting";
        Client client = visits.get(0).getClient();
        VisitSortCriterion criterion = VisitSortCriterion.PRICE;

        Mockito.doReturn(client).when(clientDao).getByPrimaryKey(1);
        Mockito.doThrow(new BusinessException(message)).when(visitDao).getSortedClientVisits(client, VisitSortCriterion.PRICE);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.getSortedClientVisits(1, criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_getSortedClientVisits_BusinessException_noClient() {
        String message = "Error at getting visits: No such client";

        Mockito.doReturn(null).when(clientDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.getSortedClientVisits(0,  VisitSortCriterion.PRICE));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_exportVisits_BusinessException_byGetting() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(visitDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.exportVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_exportVisits_BusinessException_byWriting() {
        String message = "Error at writing";
        Mockito.doReturn(visits).when(visitDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(visitReaderWriter).writeVisits(visits);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.exportVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_importClients_Exception_byReading() {
        String message = "Error at reading";
        Mockito.doThrow(new BusinessException(message)).when(visitReaderWriter).readVisits();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.importVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_importClients_Exception_byAdding() {
        String message = "Error at adding visit";
        Visit visit = visits.get(0);
        Mockito.doReturn(visit.getClient()).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(visit.getService()).when(hotelServiceDao).getByPrimaryKey(1);
        Mockito.doThrow(new BusinessException(message)).when(visitDao).add(visits.get(0));
        Mockito.doReturn(visits).when(visitReaderWriter).readVisits();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitService.importVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
