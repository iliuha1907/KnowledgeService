package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.service.reservation.ReservationService;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    public String addReservationForExistingClient(final Integer clientId, final Date arrivalDate,
                                                  final Date departureDate) {
        try {
            reservationService.addReservationForExistingClient(clientId, arrivalDate, departureDate);
            return "Successfully added reservation";
        } catch (Exception ex) {
            return "Error at adding reservation: " + ex.getMessage();
        }
    }

    public String deactivateReservation(final Integer clientId, final  Integer roomId) {
        try {
            reservationService.deactivateReservation(clientId, roomId);
            return "Successfully deactivated reservation";
        } catch (Exception ex) {
            return "Error at deactivating reservation: " + ex.getMessage();
        }
    }

    public String getReservationsExpiredAfterDate(final Date date) {
        List<Reservation> reservations;
        try {
            reservations = reservationService.getReservationsExpiredAfterDate(date);
        } catch (Exception ex) {
            return "Error at getting reservations: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Reservations:\n");
        reservations.forEach(reservation ->
                result.append(reservation).append("\n")
        );
        return result.toString();
    }

    public String getSortedReservations(final ReservationSortCriterion criterion) {
        List<Reservation> reservations;
        try {
            reservations = reservationService.getSortedReservations(criterion);
        } catch (Exception ex) {
            return "Error at getting reservations: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Reservations:\n");
        reservations.forEach(reservation ->
                result.append(reservation).append("\n")
        );
        return result.toString();
    }

    public String getLastRoomReservations(final Integer roomId) {
        List<Reservation> reservations;
        try {
            reservations = reservationService.getLastRoomReservations(roomId);
        } catch (Exception ex) {
            return "Error at getting reservations: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Reservations:\n");
        reservations.forEach(reservation ->
                result.append(reservation).append("\n")
        );
        return result.toString();
    }

    public String getNumberOfResidents() {
        try {
            return reservationService.getNumberOfResidents().toString();
        } catch (Exception ex) {
            return "Error at getting number of residents: " + ex.getMessage();
        }
    }

    public String exportReservations() {
        try {
            reservationService.exportReservations();
            return "Successfully exported reservations";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }

    public String importReservations() {
        try {
            reservationService.importReservations();
            return "Successfully imported reservations";
        } catch (BusinessException ex) {
            return ex.getMessage();
        }
    }
}

