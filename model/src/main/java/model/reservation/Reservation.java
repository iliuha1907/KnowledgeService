package model.reservation;

import model.client.Client;
import model.room.Room;

import java.util.Date;

public class Reservation {

    private Room room;
    private Client resident;
    private Date arrivalDate;
    private Date departureDate;
    private boolean isActive;

    public Reservation(final Room room, final Client resident, final Date arrivalDate, final Date departureDate,
                       final boolean isActive) {
        this.room = room;
        this.resident = resident;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.isActive = isActive;
    }

    public Room getRoom() {
        return room;
    }

    public Client getResident() {
        return resident;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return String.format("Reservation for client %s, room: %s, dates:%s - %s, is active: %s ",
                resident, room, arrivalDate, departureDate, isActive);
    }
}

