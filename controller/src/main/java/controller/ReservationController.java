package controller;

import annotation.NeedInjectionClass;
import annotation.NeedInjectionField;
import exception.BusinessException;
import model.reservation.Reservation;
import service.ReservationService;
import util.sort.ReservationSortCriterion;

import java.util.Date;
import java.util.List;

@NeedInjectionClass
public class ReservationController {

    @NeedInjectionField
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

    public String deactivateReservation(final Integer clientId) {
        try {
            reservationService.deactivateReservation(clientId);
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
            reservations = reservationService.getLastRoomVisits(roomId);
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

