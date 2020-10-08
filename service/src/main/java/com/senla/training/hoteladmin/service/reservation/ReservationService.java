package com.senla.training.hoteladmin.service.reservation;

import com.senla.training.hoteladmin.model.reservation.Reservation;

import java.util.Date;
import java.util.List;

public interface ReservationService {

    void addReservationForExistingClient(Integer clientId, Date arrivalDate, Date departureDate);

    void deactivateReservation(Integer id);

    List<Reservation> getReservationsExpiredAfterDate(Date date);

    List<Reservation> getSortedReservations(String criterion);

    List<Reservation> getLastRoomReservations(Integer roomId);

    Long getNumberOfResidents();

    void exportReservations();

    void importReservations();
}
