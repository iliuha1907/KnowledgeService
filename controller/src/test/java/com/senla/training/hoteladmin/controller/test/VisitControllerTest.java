package com.senla.training.hoteladmin.controller.test;

import com.senla.training.hoteladmin.controller.VisitController;
import com.senla.training.hoteladmin.csvapi.writeread.VisitReaderWriter;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dao.visit.VisitDao;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.VisitDto;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.dto.mapper.VisitMapper;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.visit.Visit;
import com.senla.training.hoteladmin.util.sort.VisitSortCriterion;
import config.ControllerTestConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestConfigurator.class})
class VisitControllerTest {

    private static List<Visit> visits;
    private static List<VisitDto> visitsDto;
    @Autowired
    VisitController visitController;
    @Autowired
    VisitDao visitDao;
    @Autowired
    VisitReaderWriter visitReaderWriter;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private HotelServiceDao hotelServiceDao;
    @Autowired
    VisitMapper visitMapper;
    @Autowired
    MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        Date date = Date.valueOf("2001-10-10");
        Client clientMike = new Client(1, "Mike", "Johnson");
        Client clientJohn = new Client(2, "John", "Robertson");
        HotelService hotelServiceMassage = new HotelService(1, BigDecimal.TEN, HotelServiceType.MASSAGE);
        HotelService hotelServiceSpa = new HotelService(2, BigDecimal.TEN, HotelServiceType.SPA);
        Visit visitOne = new Visit(clientMike, hotelServiceMassage);
        Visit visitTwo = new Visit(clientJohn, hotelServiceSpa);
        visits = Arrays.asList(visitOne, visitTwo);

        VisitDto visitDtoMike = new VisitDto(1, 1, 1, date, 1);
        VisitDto visitDtoJohn = new VisitDto(2, 2, 2, date, 1);
        visitsDto = Arrays.asList(visitDtoMike, visitDtoJohn);
    }

    @Test
    void VisitController_addVisit() {
        String message = "Successfully added visit";
        Visit visit = visits.get(0);
        Client client = visit.getClient();
        HotelService service = visit.getService();

        MessageDto messageDto = new MessageDto(message);
        Mockito.doReturn(client).when(clientDao).getByPrimaryKey(client.getId());
        Mockito.doReturn(service).when(hotelServiceDao).getByPrimaryKey(service.getId());
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, visitController.addVisit(visitsDto.get(0)));
    }

    @Test
    void VisitServiceImpl_addVisit_BusinessException_noClient() {
        String message = "Error at adding visit: No such client";
        Mockito.doReturn(null).when(clientDao).getByPrimaryKey(1);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.addVisit(visitsDto.get(0)));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitController_addVisit_BusinessException_noService() {
        String message = "Error at adding visit: No such hotel service";
        Mockito.doReturn(null).when(hotelServiceDao).getByPrimaryKey(1);
        Mockito.doReturn(visits.get(0).getClient()).when(clientDao).getByPrimaryKey(1);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.addVisit(visitsDto.get(0)));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitServiceImpl_getSortedClientVisits() {
        Client client = visits.get(0).getClient();
        VisitSortCriterion criterion = VisitSortCriterion.PRICE;

        Mockito.doReturn(client).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(visits).when(visitDao).getSortedClientVisits(client, VisitSortCriterion.PRICE);
        Mockito.doReturn(visitsDto).when(visitMapper).listToDto(visits);

        Assertions.assertIterableEquals(visitsDto, visitController.getSortedClientVisits(1, criterion));
    }

    @Test
    void VisitController_getSortedClientVisits_BusinessException_visitDaoError() {
        String message = "Error at getting";
        Client client = visits.get(0).getClient();
        VisitSortCriterion criterion = VisitSortCriterion.PRICE;

        Mockito.doReturn(client).when(clientDao).getByPrimaryKey(1);
        Mockito.doThrow(new BusinessException(message)).when(visitDao).getSortedClientVisits
                (client, VisitSortCriterion.PRICE);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.getSortedClientVisits(1, criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitController_getSortedClientVisits_BusinessException_noClient() {
        String message = "Error at getting visits: No such client";
        Mockito.doReturn(null).when(clientDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.getSortedClientVisits(0, VisitSortCriterion.PRICE));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitController_exportVisits() {
        String message = "Successfully exported visits";
        MessageDto messageDto = new MessageDto(message);

        Mockito.reset(visitReaderWriter);
        Mockito.doReturn(visits).when(visitDao).getAll();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, visitController.exportVisits());
    }

    @Test
    void VisitController_exportVisits_BusinessException_byGetting() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(visitDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.exportVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitController_exportVisits_BusinessException_byWriting() {
        String message = "Error at writing";
        Mockito.doReturn(visits).when(visitDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(visitReaderWriter).writeVisits(visits);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.exportVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitController_importClients() {
        String message = "Successfully imported visits";
        MessageDto messageDto = new MessageDto(message);

        Mockito.reset(visitDao);
        Mockito.doReturn(visits).when(visitReaderWriter).readVisits();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, visitController.importVisits());
    }

    @Test
    void VisitController_importClients_Exception_byReading() {
        String message = "Error at reading";
        Mockito.doThrow(new BusinessException(message)).when(visitReaderWriter).readVisits();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.importVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void VisitController_importClients_Exception_byAdding() {
        String message = "Error at adding visit";
        Visit visit = visits.get(0);
        Mockito.doReturn(visit.getClient()).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(visit.getService()).when(hotelServiceDao).getByPrimaryKey(1);
        Mockito.doThrow(new BusinessException(message)).when(visitDao).add(visits.get(0));
        Mockito.doReturn(visits).when(visitReaderWriter).readVisits();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> visitController.importVisits());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
