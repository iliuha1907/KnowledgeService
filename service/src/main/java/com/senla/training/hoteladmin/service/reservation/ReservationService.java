package com.senla.training.hoteladmin.service.reservation;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;

import java.util.Date;
import java.util.List;

public interface ReservationService {

    void addReservation(Client client, Date arrivalDate, Date departureDate);

    void addReservationForExistingClient(Integer clientId, Date arrivalDate, Date departureDate);

    void deactivateReservation(Integer clientId, Integer roomId);

    List<Reservation> getReservationsExpiredAfterDate(Date date);

    List<Reservation> getSortedReservations(ReservationSortCriterion criterion);

    List<Reservation> getLastRoomReservations(Integer roomId);

    Long getNumberOfResidents();

    void exportReservations();

    void importReservations();
}
