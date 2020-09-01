package model.visit;

import model.client.Client;
import model.hotelservice.HotelService;

import java.util.Date;

public class Visit {

    private Client client;
    private HotelService hotelService;
    private Date date;
    private boolean isActive;

    public Visit(final Client client, final HotelService hotelService, final Date date, final boolean isActive) {
        this.client = client;
        this.hotelService = hotelService;
        this.date = date;
        this.isActive = isActive;
    }

    public Client getClient() {
        return client;
    }

    public HotelService getHotelService() {
        return hotelService;
    }

    public Date getDate() {
        return date;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return String.format("Client: %s, service: %s, date: %s ", client, hotelService, date);
    }
}

