package com.senla.training.hoteladmin.unit;

import com.senla.training.hoteladmin.csvapi.writeread.ReservationReaderWriter;
import com.senla.training.hoteladmin.dao.client.ClientDao;
import com.senla.training.hoteladmin.dao.reservation.ReservationDao;
import com.senla.training.hoteladmin.dao.room.RoomDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.RoomStatus;
import com.senla.training.hoteladmin.service.reservation.ReservationServiceImpl;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
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
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigurator.class)
class ReservationImplTest {

    private static List<Reservation> reservations;
    @Autowired
    private ReservationServiceImpl reservationService;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private ReservationReaderWriter reservationReaderWriter;

    @BeforeAll
    public static void setUp() {
        java.sql.Date date = new java.sql.Date(100);
        Client clientMike = new Client(1, "Mike", "Johnson");
        Client clientJohn = new Client(2, "John", "Robertson");
        Room roomOne = new Room(1, RoomStatus.SERVED, BigDecimal.TEN, 3, 5, true);
        Room roomTwo = new Room(2, RoomStatus.SERVED, BigDecimal.TEN, 5, 5, true);
        Reservation reservationOne = new Reservation(clientMike, roomOne, date, date);
        Reservation reservationTwo = new Reservation(clientJohn, roomTwo, date, date);
        reservations = Arrays.asList(reservationOne, reservationTwo);
    }

    @Test
    void ReservationImpl_addReservationForExistingClient_BusinessException_noClient() {
        String message = "Error at adding reservation: No such client";
        Mockito.doReturn(null).when(clientDao).getById(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.addReservationForExistingClient(0, null, null));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_addReservationForExistingClient_BusinessException_noFreeRoom() {
        String message = "Error at adding reservation: No free rooms";
        Mockito.doReturn(reservations.get(0).getResident()).when(clientDao).getById(1);
        Mockito.doReturn(null).when(roomDao).getFirstFreeRoom();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.addReservationForExistingClient(1, null, null));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_addReservationForExistingClient_BusinessException_byUpdating() {
        String message = "Error at adding";
        java.sql.Date date = new java.sql.Date(100);
        Reservation reservation = reservations.get(0);
        Room room = reservation.getRoom();
        Client client = reservation.getResident();

        Mockito.doReturn(client).when(clientDao).getById(1);
        Mockito.doReturn(room).when(roomDao).getFirstFreeRoom();
        Mockito.doThrow(new BusinessException(message)).when(roomDao).update(room);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.addReservationForExistingClient(1, date, date));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_deactivateReservationForExistingClient_BusinessException() {
        String message = "Error at deactivating";
        Reservation reservation = reservations.get(0);
        Room room = reservation.getRoom();
        Client client = reservation.getResident();

        Mockito.doReturn(client).when(clientDao).getById(1);
        Mockito.doReturn(room).when(roomDao).getById(1);
        Mockito.doThrow(new BusinessException(message)).when(reservationDao)
                .deactivateClientReservation(client, room);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.deactivateReservation(1, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_deactivateReservationForExistingClient_BusinessException_noClient() {
        String message = "Error at deactivating reservation: No such entity";
        Mockito.doReturn(null).when(clientDao).getById(0);
        Mockito.doReturn(reservations.get(0).getRoom()).when(roomDao).getById(1);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.deactivateReservation(0, 1));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_deactivateReservationForExistingClient_BusinessException_noRoom() {
        String message = "Error at deactivating reservation: No such entity";
        Mockito.doReturn(reservations.get(0).getResident()).when(clientDao).getById(1);
        Mockito.doReturn(null).when(roomDao).getById(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.deactivateReservation(1, 0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_getReservationsExpiredAfterDate() {
        Date date = new Date();
        Mockito.doReturn(reservations).when(reservationDao).getReservationsExpiredAfterDate(date);

        Assertions.assertIterableEquals(reservations, reservationService.getReservationsExpiredAfterDate(date));
    }

    @Test
    void ReservationImpl_getReservationsExpiredAfterDate_BusinessException() {
        Date date = new Date();
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(reservationDao).getReservationsExpiredAfterDate(date);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.getReservationsExpiredAfterDate(date));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_getSortedReservations() {
        ReservationSortCriterion criterion = ReservationSortCriterion.NAME;
        Mockito.doReturn(reservations).when(reservationDao).getSortedReservations(criterion);

        Assertions.assertIterableEquals(reservations, reservationService.getSortedReservations(criterion));
    }

    @Test
    void ReservationImpl_getSortedReservations_BusinessException() {
        String message = "Error at getting";
        ReservationSortCriterion criterion = ReservationSortCriterion.NAME;
        Mockito.doThrow(new BusinessException(message)).when(reservationDao).getSortedReservations(criterion);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.getSortedReservations(criterion));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_getLastRoomReservations() {
        Room room = reservations.get(0).getRoom();
        reservationService.setNumberOfResidents(2);
        Mockito.doReturn(room).when(roomDao).getById(room.getId());
        Mockito.doReturn(reservations).when(reservationDao).getLastRoomReservations(room, 2);

        Assertions.assertIterableEquals(reservations, reservationService.getLastRoomReservations(room.getId()));
    }

    @Test
    void ReservationImpl_getLastRoomReservations_BusinessException() {
        String message = "Error at getting";
        Room room = reservations.get(0).getRoom();
        reservationService.setNumberOfResidents(2);
        Mockito.doThrow(new BusinessException(message)).when(reservationDao).getLastRoomReservations(room, 2);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.getLastRoomReservations(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_getLastRoomReservations_BusinessException_noRoom() {
        String message = "Error at getting reservations: No such room";
        Mockito.doReturn(null).when(roomDao).getById(0);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.getLastRoomReservations(0));

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_getNumberOfResidents() {
        Long expected = (long) reservations.size();
        Mockito.doReturn(expected).when(reservationDao).getNumberOfResidents();

        Assertions.assertEquals(expected, reservationService.getNumberOfResidents());
    }

    @Test
    void ReservationImpl_getNumberOfResidents_BusinessException() {
        String message = "Error at getting number";
        Mockito.doThrow(new BusinessException(message)).when(reservationDao)
                .getNumberOfResidents();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.getNumberOfResidents());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_exportReservations_BusinessException_byGetting() {
        String message = "Error at getting";
        Mockito.doThrow(new BusinessException(message)).when(reservationDao).getAll();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.exportReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_exportReservations_BusinessException_byWriting() {
        String message = "Error at writing";
        Mockito.doReturn(reservations).when(reservationDao).getAll();
        Mockito.doThrow(new BusinessException(message)).when(reservationReaderWriter).writeReservations(reservations);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.exportReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_importReservations_BusinessException_byReading() {
        String message = "Error at reading";
        Mockito.doThrow(new BusinessException(message)).when(reservationReaderWriter).readReservations();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.importReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void ReservationImpl_importReservations_BusinessException_byUpdatingRoom() {
        String message = "Error at updating room";
        Reservation reservation = reservations.get(0);
        Client client = reservation.getResident();
        Room room = reservation.getRoom();

        Mockito.doReturn(client).when(clientDao).getById(1);
        Mockito.doReturn(room).when(roomDao).getById(1);
        Mockito.doReturn(null).when(reservationDao).getReservationByRoomClient(client, room);
        Mockito.doReturn(reservations).when(reservationReaderWriter).readReservations();
        Mockito.doThrow(new BusinessException(message)).when(roomDao).update(room);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reservationService.importReservations());

        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
