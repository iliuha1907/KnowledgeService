package com.senla.training.hoteladmin.unit;

import com.senla.training.hoteladmin.csvapi.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
import com.senla.training.hoteladmin.model.room.Room;
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
    public static void setUp() {
        HotelService hotelServiceMassage = new HotelService(1, BigDecimal.TEN, HotelServiceType.MASSAGE);
        HotelService hotelServiceSpa = new HotelService(2, BigDecimal.TEN, HotelServiceType.SPA);
        hotelServices = Arrays.asList(hotelServiceMassage, hotelServiceSpa);
    }

    @Test
    void HotelServiceServiceImpl_updateService_BusinessException() {
        String message = "Error at updating Service";
        HotelService service = new HotelService();

        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).update(service);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.updateService(service, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_updateService_BusinessException_null() {
        String message = "Error at updating Service: Service is null";

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.updateService(null, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_getServices() {
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Assertions.assertIterableEquals(hotelServices, hotelServiceService.getServices());
    }

    @Test
    void HotelServiceServiceImpl_getServices_BusinessException() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.getServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_exportServices_BusinessException_byWriting() {
        String message = "Error at writing services";
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceReaderWriter).writeServices(hotelServices);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.exportServices());
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_exportServices_BusinessException_byGetting() {
        String message = "Error at getting services";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.exportServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_importServices_BusinessException_byReading() {
        String message = "Error at reading services";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceReaderWriter).readServices();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.importServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_importServices_BusinessException_byAdding() {
        String message = "Error at adding service";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).add(hotelServices.get(0));
        Mockito.doReturn(hotelServices).when(hotelServiceReaderWriter).readServices();

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceService.importServices());
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
