package com.senla.training.hoteladmin.model.room;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private RoomStatus status;
    private BigDecimal price;
    private Integer capacity;
    private Integer stars;
    private Client resident;

    public Room() {
    }

    public Room(RoomStatus status, BigDecimal price, Integer capacity,
                Integer stars) {
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        resident = null;
    }

    public Room(Integer id, RoomStatus status, BigDecimal price, Integer capacity,
                Integer stars) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        resident = null;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setResident(Client resident) {
        this.resident = resident;
    }

    public Integer getId() {
        return id;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Client getResident() {
        return resident;
    }

    public Integer getStars() {
        return stars;
    }

    public Integer getCapacity() {
        return capacity;
    }

    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        return (obj instanceof Room) && this.id.equals(((Room) obj).getId());
    }

    @Override
    public String toString() {
        String result = String.format("Room with idspread:%d, status:%s, capacity: %d, stars: %d, price: %.2f",
                id, status.toString(), capacity, stars, price);
        if (resident != null) {
            result += ", taken from " + DateUtil.getString(resident.getArrivalDate()) +
                    " to " + DateUtil.getString(resident.getDepartureDate());
        }
        return result;
    }
}

