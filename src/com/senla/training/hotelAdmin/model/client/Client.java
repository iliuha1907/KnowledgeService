package com.senla.training.hoteladmin.model.client;

import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {
    private static final long SerialVersionUID = 1l;
    private Integer id;
    private String firstName;
    private String lastName;
    private Date arrivalDate;
    private Date departureDate;
    private Room room;

    public Client() {
    }

    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(String firstName, String lastName,
                  Date arrivalDate, Date departureDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public Client(Integer id, String firstName, String lastName,
                  Date arrivalDate, Date departureDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoom(Room room) {
        this.room = room;
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

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        return (obj instanceof Client) && (this.id.equals(((Client) obj).getId()));
    }

    @Override
    public String toString() {
        return String.format("Client %s %s, id:%d, arrival date:%s, departure date: %s, " +
                        "\nroom:%s ", firstName, lastName, id, DateUtil.getString(arrivalDate),
                DateUtil.getString(departureDate), room.toString());
    }
}

