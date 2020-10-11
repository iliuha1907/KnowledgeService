package com.senla.training.hoteladmin.controller.test;

import com.senla.training.hoteladmin.controller.RoomController;
import com.senla.training.hoteladmin.csvapi.writeread.RoomReaderWriter;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.RoomDto;
import com.senla.training.hoteladmin.dto.config.DtoMapperConfiguration;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.dto.mapper.RoomMapper;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.service.room.RoomService;
import com.senla.training.hoteladmin.service.room.RoomServiceImpl;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
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
public class RoomControllerTest {

    private static List<Room> rooms;
    private static List<RoomDto> roomsDto;
    @Autowired
    RoomController roomController;
    @Autowired
    RoomDao roomDao;
    @Autowired
    RoomReaderWriter roomReaderWriter;
    @Autowired
    RoomMapper roomMapper;
    @Autowired
    RoomService roomService;
    @Autowired
    MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        Room roomOne = new Room(1, RoomStatus.SERVED, BigDecimal.TEN, 3, 5, true);
        Room roomTwo = new Room(2, RoomStatus.SERVED, BigDecimal.TEN, 5, 5, true);
        rooms = Arrays.asList(roomOne, roomTwo);

        RoomDto roomDtoOne = new RoomDto(1, RoomStatus.SERVED, BigDecimal.TEN, 3, 5, 1);
        RoomDto roomDtoTwo = new RoomDto(2, RoomStatus.SERVED, BigDecimal.TEN, 5, 5, 1);
        roomsDto = Arrays.asList(roomDtoOne, roomDtoTwo);
    }

    @Test
    void RoomController_addRoom() {
        String message = "Successfully added room";
        MessageDto messageDto = new MessageDto(message);

        Mockito.doReturn(rooms).when(roomDao).getAll();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, roomController.addRoom(roomsDto.get(0)));
    }

    @Test
    void RoomController_getSortedRooms() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;

        Mockito.doReturn(rooms).when(roomDao).getSortedRooms(RoomsSortCriterion.CAPACITY);
        Mockito.doReturn(roomsDto).when(roomMapper).listToDto(rooms);

        Assertions.assertIterableEquals(roomsDto, roomController.getRooms(criterion));
    }

    @Test
    void RoomController_updateRoom_BusinessException() {
        String message = "Error at updating Room";
        RoomDto roomDto = roomsDto.get(0);
        Room room = rooms.get(0);

        Mockito.doReturn(room).when(roomMapper).toEntity(roomDto);
        Mockito.doThrow(new BusinessException(message)).when(roomDao).update(room);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.updateRoom(roomDto, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_updateRoom_BusinessException_null() {
        String message = "Error at updating Room: Room is null";

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.updateRoom(null, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_updateRoom_BusinessException_noPermission() {
        String message = "Error at updating Room: No permission to change status";
        ((RoomServiceImpl) roomService).setChangeableStatus(false);
        RoomDto roomDto = roomsDto.get(0);

        Mockito.doReturn(rooms.get(0)).when(roomMapper).toEntity(roomDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.updateRoom(roomsDto.get(0), 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_getRooms() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;
        RoomsSortCriterion roomsSortCriterion = RoomsSortCriterion.CAPACITY;

        Mockito.doReturn(rooms).when(roomDao).getSortedRooms(roomsSortCriterion);
        Mockito.doReturn(roomsDto).when(roomMapper).listToDto(rooms);

        Assertions.assertEquals(roomsDto, roomController.getRooms(criterion));
    }

    @Test
    void RoomController_getRooms_BusinessException() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;
        String message = "Error at getting";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).getSortedRooms(RoomsSortCriterion.CAPACITY);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.getRooms(criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_getFreeRooms() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;

        Mockito.doReturn(rooms).when(roomDao).getSortedFreeRooms(criterion);
        Mockito.doReturn(roomsDto).when(roomMapper).listToDto(rooms);

        Assertions.assertIterableEquals(roomsDto, roomController.getFreeRooms(criterion));
    }

    @Test
    void RoomController_getFreeRooms_naturalCriterion() {
        RoomsSortCriterion criterion = RoomsSortCriterion.NATURAL;
        RoomsSortCriterion roomsSortCriterion = RoomsSortCriterion.PRICE;

        Mockito.doReturn(rooms).when(roomDao).getFreeRooms();
        Mockito.doReturn(roomsDto).when(roomMapper).listToDto(rooms);

        Assertions.assertIterableEquals(roomsDto, roomController.getFreeRooms(criterion));
    }

    @Test
    void RoomServiceImpl_getFreeRooms_naturalCriterion_BusinessException() {
        RoomsSortCriterion criterion = RoomsSortCriterion.NATURAL;
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(roomDao).getFreeRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.getFreeRooms(criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getFreeRooms_BusinessException() {
        RoomsSortCriterion criterion = RoomsSortCriterion.PRICE;
        String message = "Error at getting";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).getSortedFreeRooms(criterion);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.getFreeRooms(criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_getPriceRoom() {
        Room room = rooms.get(0);
        Mockito.doReturn(room).when(roomDao).getByPrimaryKey(1);

        Assertions.assertEquals(room.getPrice(), roomController.getPriceRoom(1));
    }

    @Test
    void RoomController_getPriceRoom_BusinessException_noRoom() {
        String message = "Error at getting Room price: no such room!";

        Mockito.doReturn(null).when(roomDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.getPriceRoom(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_getRoom() {
        Room room = rooms.get(0);
        RoomDto roomDto = roomsDto.get(0);

        Mockito.doReturn(room).when(roomDao).getByPrimaryKey(1);
        Mockito.doReturn(roomDto).when(roomMapper).toDto(room);

        Assertions.assertEquals(roomDto, roomController.getRoom(1));
    }

    @Test
    void RoomController_getRoom_BusinessException() {
        String message = "Error at getting";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.getRoom(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_getRoom_BusinessException_noRoom() {
        String message = "Error at getting Room: no such entity!";

        Mockito.doReturn(null).when(roomDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.getRoom(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_getNumberOfFreeRooms() {
        Long count = (long) rooms.size();

        Mockito.doReturn(count).when(roomDao).getNumberOfFreeRooms();

        Assertions.assertEquals(count, roomController.getNumberOfFreeRooms());
    }

    @Test
    void RoomController_getNumberOfFreeRooms_BusinessException() {
        String message = "Error at getting number";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).getNumberOfFreeRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.getNumberOfFreeRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_exportRooms_BusinessException_byGetting() {
        String message = "Error at getting";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.exportRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_exportRooms_BusinessException_byWriting() {
        String message = "Error at writing";

        Mockito.doReturn(rooms).when(roomDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(roomReaderWriter).writeRooms(rooms);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.exportRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_importClients_BusinessException_byReading() {
        String message = "Error at reading";

        Mockito.doThrow(new BusinessException(message)).when(roomReaderWriter).readRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.importRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomController_importClients_BusinessException_byAdding() {
        String message = "Error at adding client";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).add(rooms.get(0));
        Mockito.doReturn(rooms).when(roomReaderWriter).readRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomController.importRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
