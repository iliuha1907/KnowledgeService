package com.senla.training.hoteladmin.unit;

import com.senla.training.hoteladmin.csvapi.writeread.RoomReaderWriter;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.service.room.RoomServiceImpl;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
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
class RoomServiceImplTest {

    private static List<Room> rooms;
    @Autowired
    private RoomServiceImpl roomService;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomReaderWriter roomReaderWriter;

    @BeforeAll
    public static void setUp() {
        Room roomOne = new Room(1, RoomStatus.SERVED, BigDecimal.TEN, 3, 5, true);
        Room roomTwo = new Room(2, RoomStatus.SERVED, BigDecimal.TEN, 5, 5, true);
        rooms = Arrays.asList(roomOne, roomTwo);
    }

    @Test
    void RoomServiceImpl_getSortedRooms() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;

        Mockito.doReturn(rooms).when(roomDao).getSortedRooms(criterion);

        Assertions.assertIterableEquals(rooms, roomService.getSortedRooms(criterion));
    }

    @Test
    void RoomServiceImpl_updateRoom_BusinessException() {
        String message = "Error at updating Room";
        Room room = new Room();

        Mockito.doThrow(new BusinessException(message)).when(roomDao).update(room);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.updateRoom(room, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_updateRoom_BusinessException_null() {
        String message = "Error at updating Room: Room is null";

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.updateRoom(null, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_updateRoom_BusinessException_noPermission() {
        String message = "Error at updating Room: No permission to change status";
        roomService.setChangeableStatus(false);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.updateRoom(new Room(), 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getSortedRooms_BusinessException() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;
        String message = "Error at getting";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).getSortedRooms(RoomsSortCriterion.CAPACITY);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.getSortedRooms(criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getFreeRooms() {
        Mockito.doReturn(rooms).when(roomDao).getFreeRooms();
        Assertions.assertIterableEquals(rooms, roomService.getFreeRooms());
    }

    @Test
    void RoomServiceImpl_getFreeRooms_BusinessException() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(roomDao).getFreeRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.getFreeRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getSortedFreeRooms() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;
        Mockito.doReturn(rooms).when(roomDao).getSortedFreeRooms(RoomsSortCriterion.CAPACITY);

        Assertions.assertIterableEquals(rooms, roomService.getSortedFreeRooms(criterion));
    }

    @Test
    void RoomServiceImpl_getSortedFreeRooms_BusinessException() {
        RoomsSortCriterion criterion = RoomsSortCriterion.CAPACITY;
        String message = "Error at getting";

        Mockito.doThrow(new BusinessException(message)).when(roomDao).getSortedFreeRooms(criterion);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.getSortedFreeRooms(criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getPriceRoom() {
        Room room = rooms.get(0);
        Mockito.doReturn(room).when(roomDao).getByPrimaryKey(1);

        Assertions.assertEquals(room.getPrice(), roomService.getPriceRoom(1));
    }

    @Test
    void RoomServiceImpl_getPriceRoom_BusinessException_noRoom() {
        String message = "Error at getting Room price: no such room!";
        Mockito.doReturn(null).when(roomDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.getPriceRoom(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getRoom() {
        Room room = rooms.get(0);
        Mockito.doReturn(room).when(roomDao).getByPrimaryKey(1);

        Assertions.assertEquals(room, roomService.getRoom(1));
    }

    @Test
    void RoomServiceImpl_getRoom_BusinessException() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(roomDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.getRoom(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getRoom_BusinessException_noRoom() {
        String message = "Error at getting Room: no such entity!";
        Mockito.doReturn(null).when(roomDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.getRoom(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_getNumberOfFreeRooms() {
        Long count = (long) rooms.size();
        Mockito.doReturn(count).when(roomDao).getNumberOfFreeRooms();

        Assertions.assertEquals(count, roomService.getNumberOfFreeRooms());
    }

    @Test
    void RoomServiceImpl_getNumberOfFreeRooms_BusinessException() {
        String message = "Error at getting number";
        Mockito.doThrow(new BusinessException(message)).when(roomDao).getNumberOfFreeRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.getNumberOfFreeRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_exportRooms_BusinessException_byGetting() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(roomDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.exportRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_exportRooms_BusinessException_byWriting() {
        String message = "Error at writing";
        Mockito.doReturn(rooms).when(roomDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(roomReaderWriter).writeRooms(rooms);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.exportRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_importClients_BusinessException_byReading() {
        String message = "Error at reading";
        Mockito.doThrow(new BusinessException(message)).when(roomReaderWriter).readRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.importRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void RoomServiceImpl_importClients_BusinessException_byAdding() {
        String message = "Error at adding client";
        Mockito.doThrow(new BusinessException(message)).when(roomDao).add(rooms.get(0));
        Mockito.doReturn(rooms).when(roomReaderWriter).readRooms();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> roomService.importRooms());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
