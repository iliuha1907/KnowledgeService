package com.senla.training.hoteladmin.unit;

import com.senla.training.hoteladmin.csvapi.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.service.hotelservice.HotelServiceServiceImpl;
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
class HotelServiceServiceImplTest {

    private static List<HotelService> hotelServices;
    @Autowired
    private HotelServiceServiceImpl hotelServiceService;
    @Autowired
    private HotelServiceDao hotelServiceDao;
    @Autowired
    private HotelServiceReaderWriter hotelServiceReaderWriter;

    @BeforeAll
    public static void setUp(){
        HotelService hotelServiceMassage = new HotelService(1,BigDecimal.TEN, HotelServiceType.MASSAGE);
        HotelService hotelServiceSpa = new HotelService(2,BigDecimal.TEN, HotelServiceType.SPA);
        hotelServices = Arrays.asList(hotelServiceMassage, hotelServiceSpa);
    }

    @Test
    void HotelServiceImpl_setServicePrice_BusinessException_noRoom(){
        String message = "Error at setting service price: no such service";
        Mockito.doReturn(null).when(hotelServiceDao).getById(0);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.setServicePrice(0, BigDecimal.TEN));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceImpl_setServicePrice_BusinessException_byUpdating(){
        String message = "Could not update";
        HotelService hotelService = hotelServices.get(0);
        Mockito.doReturn(hotelService).when(hotelServiceDao).getById(1);
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).update(hotelService);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.setServicePrice(1, BigDecimal.TEN));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceImpl_getServices() {
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Assertions.assertIterableEquals(hotelServices, hotelServiceService.getServices());
    }

    @Test
    void ClientServiceImpl_getServices_BusinessException() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.getServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceImpl_exportServices_BusinessException_byWriting(){
        String message = "Error at writing services";
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceReaderWriter).writeServices(hotelServices);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.exportServices());
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientServiceImpl_exportServices_BusinessException_byGetting(){
        String message = "Error at getting services";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.exportServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ClientServiceImpl_importServices_BusinessException_byReading(){
        String message = "Error at reading services";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceReaderWriter).readServices();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.importServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceImpl_importServices_BusinessException_byAdding(){
        String message = "Error at adding service";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).add(hotelServices.get(0));
        Mockito.doReturn(hotelServices).when(hotelServiceReaderWriter).readServices();

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.importServices());
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
