package com.senla.training.hoteladmin.model.reservation;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;

import java.util.Date;

public class Reservation {
    private Room room;
    private Client resident;
    private Date arrivalDate;
    private Date departureDate;
    private boolean isActive;

    public Reservation(Room room, Client resident, Date arrivalDate, Date departureDate,
                       boolean isActive) {
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

