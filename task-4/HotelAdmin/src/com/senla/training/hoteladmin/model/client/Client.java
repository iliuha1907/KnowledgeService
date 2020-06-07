package com.senla.training.hoteladmin.model.client;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    private int passportNumber;
    private String firstName;
    private String lastName;
    private Date arrivalDate;
    private Date departureDate;
    private Room room;

    public Client(int passportNumber, String firstName, String lastName) {
        this.passportNumber = passportNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getPassportNumber() {
        return passportNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Client) && (this.passportNumber == ((Client) obj).getPassportNumber());
    }

    @Override
    public int hashCode() {
        return passportNumber;
    }

    @Override
    public String toString() {
        return String.format("Client %s %s, number of passport: %d, arrival date:%s, departure date: %s, " +
                        "\nroom:%s ", firstName, lastName, passportNumber, DateUtil.getStr(arrivalDate),
                DateUtil.getStr(departureDate), room.toString());
    }
}

