package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.util.sort.ReservationSortCriterion;
import com.senla.training.injection.annotation.NeedInjectionClass;
import com.senla.training.injection.annotation.NeedInjectionField;
import com.senla.training.hoteladmin.service.ReservationService;

import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationController {
    @NeedInjectionField
    private ReservationService reservationService;

    public String addReservationForExistingClient(Integer clientId, Date arrivalDate, Date departureDate) {
        try {
            reservationService.addReservationForExistingClient(clientId, arrivalDate, departureDate);
            return "Successfully added reservation";
        } catch (Exception ex) {
            return "Error at adding reservation: " + ex.getMessage();
        }
    }

    public String deactivateReservation(Integer clientId) {
        try {
            reservationService.deactivateReservation(clientId);
            return "Successfully deactivated reservation";
        } catch (Exception ex) {
            return "Error at deactivating reservation: " + ex.getMessage();
        }
    }

    public String getReservationsExpiredAfterDate(Date date) {
        List<Reservation> reservations;
        try {
            reservations = reservationService.getReservationsExpiredAfterDate(date);
        } catch (Exception ex) {
            return "Error at getting reservations: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Reservations:\n");
        reservations.forEach(reservation -> {
            result.append(reservation).append("\n");
        });
        return result.toString();
    }

    public String getSortedReservations(ReservationSortCriterion criterion) {
        List<Reservation> reservations;
        try {
            reservations = reservationService.getSortedReservations(criterion);
        } catch (Exception ex) {
            return "Error at getting reservations: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Reservations:\n");
        reservations.forEach(reservation -> {
            result.append(reservation).append("\n");
        });
        return result.toString();
    }

    public String getLastRoomReservations(Integer roomId) {
        List<Reservation> reservations;
        try {
            reservations = reservationService.getLastRoomVisits(roomId);
        } catch (Exception ex) {
            return "Error at getting reservations: " + ex.getMessage();
        }

        StringBuilder result = new StringBuilder("Reservations:\n");
        reservations.forEach(reservation -> {
            result.append(reservation).append("\n");
        });
        return result.toString();
    }

    public String getNumberOfResidents() {
        try {
            return reservationService.getNumberOfResidents().toString();
        } catch (Exception ex) {
            return "Error at getting number of residents: " + ex.getMessage();
        }
    }
}

