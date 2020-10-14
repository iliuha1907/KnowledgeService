package com.senla.training.hoteladmin.controller.test;

import com.senla.training.hoteladmin.controller.HotelServiceController;
import com.senla.training.hoteladmin.csvapi.writeread.HotelServiceReaderWriter;
import com.senla.training.hoteladmin.dao.hotelservice.HotelServiceDao;
import com.senla.training.hoteladmin.dto.HotelServiceDto;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.config.DtoMapperConfiguration;
import com.senla.training.hoteladmin.dto.mapper.HotelServiceMapper;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;
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
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestConfigurator.class, DtoMapperConfiguration.class})
public class HotelServiceControllerTest {

    private static List<HotelService> hotelServices;
    private static List<HotelServiceDto> hotelServicesDto;
    @Autowired
    HotelServiceController hotelServiceController;
    @Autowired
    HotelServiceDao hotelServiceDao;
    @Autowired
    HotelServiceReaderWriter hotelServiceReaderWriter;
    @Autowired
    HotelServiceMapper hotelServiceMapper;
    @Autowired
    MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        HotelService hotelServiceMassage = new HotelService(1, BigDecimal.TEN, HotelServiceType.MASSAGE);
        HotelService hotelServiceSpa = new HotelService(2, BigDecimal.TEN, HotelServiceType.SPA);
        hotelServices = Arrays.asList(hotelServiceMassage, hotelServiceSpa);

        HotelServiceDto hotelServiceDtoMassage = new HotelServiceDto(1, BigDecimal.TEN, HotelServiceType.MASSAGE);
        HotelServiceDto hotelServiceDtoSpa = new HotelServiceDto(2, BigDecimal.TEN, HotelServiceType.SPA);
        hotelServicesDto = Arrays.asList(hotelServiceDtoMassage, hotelServiceDtoSpa);
    }

    @Test
    void HotelServiceController_addService() {
        String message = "Successfully added service";
        HotelServiceDto serviceDto = hotelServicesDto.get(0);
        MessageDto messageDto = new MessageDto(message);
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, hotelServiceController.addService(serviceDto));
    }

    @Test
    void HotelServiceController_updateService() {
        String message = "Successfully updated service";
        HotelService service = hotelServices.get(0);
        HotelServiceDto serviceDto = hotelServicesDto.get(0);
        MessageDto messageDto = new MessageDto(message);

        Mockito.reset(hotelServiceDao);
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);
        Mockito.doReturn(service).when(hotelServiceMapper).toEntity(serviceDto);

        Assertions.assertEquals(messageDto, hotelServiceController.updateService(serviceDto, 1));
    }

    @Test
    void HotelServiceController_updateService_BusinessException() {
        String message = "Error at updating Service";
        HotelService service = hotelServices.get(0);
        HotelServiceDto serviceDto = hotelServicesDto.get(0);

        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).update(service);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceController.updateService(serviceDto, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceController_updateService_BusinessException_null() {
        String message = "Error at updating Service: Service is null";

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceController.updateService(null, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceController_getServices() {
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Mockito.doReturn(hotelServicesDto).when(hotelServiceMapper).listToDto(hotelServices);

        Assertions.assertIterableEquals(hotelServicesDto, hotelServiceController.getServices());
    }

    @Test
    void HotelServiceController_getServices_BusinessException() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceController.getServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceController_exportServices() {
        String message = "Successfully exported services";
        MessageDto messageDto = new MessageDto(message);

        Mockito.reset(hotelServiceReaderWriter);
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, hotelServiceController.exportServices());
    }

    @Test
    void HotelServiceController_exportServices_BusinessException_byWriting() {
        String message = "Error at writing services";
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceReaderWriter).writeServices(hotelServices);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceController.exportServices());
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceController_exportServices_BusinessException_byGetting() {
        String message = "Error at getting services";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceController.exportServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_importServices() {
        String message = "Successfully imported services";
        MessageDto messageDto = new MessageDto(message);
        Mockito.reset(hotelServiceDao);
        Mockito.doReturn(hotelServices).when(hotelServiceDao).getAll();
        Mockito.doReturn(hotelServices).when(hotelServiceReaderWriter).readServices();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);


        Assertions.assertEquals(messageDto, hotelServiceController.importServices());
    }

    @Test
    void HotelServiceServiceImpl_importServices_BusinessException_byReading() {
        String message = "Error at reading services";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceReaderWriter).readServices();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceController.importServices());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void HotelServiceServiceImpl_importServices_BusinessException_byAdding() {
        String message = "Error at adding service";
        Mockito.doThrow(new BusinessException(message)).when(hotelServiceDao).add(hotelServices.get(0));
        Mockito.doReturn(hotelServices).when(hotelServiceReaderWriter).readServices();

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> hotelServiceController.importServices());
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
