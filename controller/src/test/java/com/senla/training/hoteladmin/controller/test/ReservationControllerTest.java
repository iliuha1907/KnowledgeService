package com.senla.training.hoteladmin.controller.test;

import com.senla.training.hoteladmin.controller.ReservationController;
import com.senla.training.hoteladmin.csvapi.writeread.ReservationReaderWriter;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.ReservationDto;
import com.senla.training.hoteladmin.dto.mapper.MessageDtoMapper;
import com.senla.training.hoteladmin.dto.mapper.ReservationMapper;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.service.reservation.ReservationService;
import com.senla.training.hoteladmin.service.reservation.ReservationServiceImpl;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
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
class ReservationControllerTest {

    private static List<Reservation> reservations;
    private static List<ReservationDto> reservationsDto;
    @Autowired
    ReservationController reservationController;
    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationDao reservationDao;
    @Autowired
    ClientDao clientDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    ReservationReaderWriter reservationReaderWriter;
    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        Date dateSql = Date.valueOf("2001-10-10");
        Client clientMike = new Client(1, "Mike", "Johnson");
        Client clientJohn = new Client(2, "John", "Robertson");
        Room roomOne = new Room(1, RoomStatus.SERVED, BigDecimal.TEN, 3, 5, true);
        Room roomTwo = new Room(2, RoomStatus.SERVED, BigDecimal.TEN, 5, 5, true);
        Reservation reservationOne = new Reservation(roomOne, clientMike, dateSql, dateSql, 1);
        Reservation reservationTwo = new Reservation(roomTwo, clientJohn, dateSql, dateSql, 1);
        reservations = Arrays.asList(reservationOne, reservationTwo);

        ReservationDto reservationDtoMike = new ReservationDto(1, dateSql, dateSql, 1, 1, 1);
        ReservationDto reservationDtoJohn = new ReservationDto(2, dateSql, dateSql, 1, 2, 2);
        reservationsDto = Arrays.asList(reservationDtoMike, reservationDtoJohn);
    }

    @Test
    void RoomController_addReservation() {
        String message = "Successfully added reservation";
        Reservation reservation = reservations.get(0);
        ReservationDto reservationDto = reservationsDto.get(0);
        MessageDto messageDto = new MessageDto(message);

        Mockito.doReturn(reservation.getResident()).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(reservation.getRoom()).when(roomDao).getFirstFreeRoom();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, reservationController.addReservation(reservationDto));
    }

    @Test
    void ReservationController_addReservation_BusinessException_noClient() {
        String message = "Error at adding reservation: No such client";

        Mockito.doReturn(null).when(clientDao).getByPrimaryKey(1);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.addReservation(reservationsDto.get(0)));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_addReservation_BusinessException_noFreeRoom() {
        String message = "Error at adding reservation: No free rooms";

        Mockito.doReturn(reservations.get(0).getResident()).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(null).when(roomDao).getFirstFreeRoom();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.addReservation(reservationsDto.get(0)));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_addReservation_BusinessException_byUpdating() {
        String message = "Error at adding";

        java.sql.Date date = new java.sql.Date(100);
        Reservation reservation = reservations.get(0);
        Room room = reservation.getRoom();
        Client client = reservation.getResident();

        Mockito.doReturn(client).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(room).when(roomDao).getFirstFreeRoom();
        Mockito.doThrow(new BusinessException(message)).when(roomDao).update(room);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.addReservation(reservationsDto.get(0)));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_getReservations() {
        ReservationSortCriterion criterion = ReservationSortCriterion.NAME;
        Date date = new Date(0);

        Mockito.doReturn(reservations).when(reservationDao).getSortedReservations(criterion, date);
        Mockito.doReturn(reservationsDto).when(reservationMapper).listToDto(reservations);

        Assertions.assertIterableEquals(reservationsDto, reservationController.getReservations(criterion, date));
    }

    @Test
    void ReservationController_getReservations_dateNull() {
        ReservationSortCriterion criterion = ReservationSortCriterion.NAME;
        Date date = null;

        Mockito.doReturn(reservations).when(reservationDao).getSortedReservations(criterion, date);
        Mockito.doReturn(reservationsDto).when(reservationMapper).listToDto(reservations);

        Assertions.assertIterableEquals(reservationsDto, reservationController.getReservations(criterion, date));
    }

    @Test
    void ReservationController_deactivateReservation() {
        String message = "Successfully deactivated reservation";
        Reservation reservation = reservations.get(0);
        MessageDto messageDto = new MessageDto(message);

        Mockito.reset(reservationDao);
        Mockito.doReturn(reservation).when(reservationDao).getByPrimaryKey(1);
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, reservationController.deactivateReservation(1));
    }

    @Test
    void ReservationController_deactivateReservation_BusinessException_reservationDaoError() {
        String message = "Error at deactivating reservation";
        Reservation reservation = reservations.get(0);

        Mockito.doReturn(reservation).when(reservationDao).getByPrimaryKey(1);
        Mockito.doThrow(new BusinessException(message)).when(reservationDao).update(reservation);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.deactivateReservation(1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_deactivateReservation_BusinessException_noReservation() {
        String message = "Error at deactivating reservation: no such entity!";

        Mockito.doReturn(null).when(reservationDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.deactivateReservation(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_getLastRoomReservations() {
        Room room = reservations.get(0).getRoom();
        Integer roomId = room.getId();
        ;

        ((ReservationServiceImpl) reservationService).setNumberOfResidents(2);
        Mockito.doReturn(room).when(roomDao).getByPrimaryKey(roomId);
        Mockito.doReturn(reservations).when(reservationDao).getLastRoomReservations(room, 2);
        Mockito.doReturn(reservationsDto).when(reservationMapper).listToDto(reservations);

        Assertions.assertIterableEquals(reservationsDto, reservationController.getLastRoomReservations(roomId));
    }

    @Test
    void ReservationController_getLastRoomReservations_BusinessException_reservationDaoError() {
        String message = "Error at getting";
        Room room = reservations.get(0).getRoom();

        ((ReservationServiceImpl) reservationService).setNumberOfResidents(2);
        Mockito.doThrow(new BusinessException(message)).when(reservationDao)
                .getLastRoomReservations(room, 2);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.getLastRoomReservations(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_getLastRoomReservations_BusinessException_noRoom() {
        String message = "Error at getting reservations: No such room";

        Mockito.doReturn(null).when(roomDao).getByPrimaryKey(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.getLastRoomReservations(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_getNumberOfResidents() {
        Long expected = (long) reservations.size();

        Mockito.doReturn(expected).when(reservationDao).getNumberOfResidents();

        Assertions.assertEquals(expected, reservationController.getNumberOfResidents());
    }

    @Test
    void ReservationController_getNumberOfResidents_BusinessException_reservationDaoError() {
        String message = "Error at getting number";

        Mockito.doThrow(new BusinessException(message)).when(reservationDao)
                .getNumberOfResidents();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.getNumberOfResidents());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_exportReservations() {
        String message = "Successfully exported reservations";
        MessageDto messageDto = new MessageDto(message);

        Mockito.reset(reservationDao);
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, reservationController.exportReservations());
    }

    @Test
    void ReservationController_exportReservations_BusinessException_byGetting() {
        String message = "Error at getting";

        Mockito.doThrow(new BusinessException(message)).when(reservationDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.exportReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_exportReservations_BusinessException_byWriting() {
        String message = "Error at writing";

        Mockito.doThrow(new BusinessException(message)).when(reservationReaderWriter)
                .writeReservations(reservations);
        Mockito.doReturn(reservations).when(reservationDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.exportReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_importReservations() {
        String message = "Successfully imported reservations";
        MessageDto messageDto = new MessageDto(message);

        Mockito.doReturn(reservations).when(reservationReaderWriter).readReservations();
        Mockito.doReturn(messageDto).when(messageDtoMapper).toDto(message);

        Assertions.assertEquals(messageDto, reservationController.importReservations());
    }

    @Test
    void ReservationController_importReservations_BusinessException_byReading() {
        String message = "Error at reading";

        Mockito.doThrow(new BusinessException(message)).when(reservationReaderWriter).readReservations();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.importReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationController_importReservations_BusinessException_byUpdatingRoom() {
        String message = "Error at updating room";
        Reservation reservation = reservations.get(0);
        Client client = reservation.getResident();
        Room room = reservation.getRoom();
        room.setIsFree(1);

        Mockito.doReturn(reservations).when(reservationReaderWriter).readReservations();
        Mockito.doReturn(client).when(clientDao).getByPrimaryKey(1);
        Mockito.doReturn(room).when(roomDao).getByPrimaryKey(1);
        Mockito.doReturn(null).when(reservationDao).getReservationByRoomClient(client, room);
        Mockito.doThrow(new BusinessException(message)).when(roomDao).update(room);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationController.importReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

}
