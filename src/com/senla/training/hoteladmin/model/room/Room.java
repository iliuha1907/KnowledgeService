package com.senla.training.hoteladmin.model.room;

import java.io.Serializable;
import java.math.BigDecimal;

public class Room implements Serializable {
    private Integer id;
    private RoomStatus status;
    private BigDecimal price;
    private Integer capacity;
    private Integer stars;
    private boolean isFree;

    public Room(Integer id, RoomStatus status, BigDecimal price, Integer capacity,
                Integer stars, boolean isFree) {
        this.id = id;
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.isFree = isFree;
    }

    public Room(RoomStatus status, BigDecimal price, Integer capacity,
                Integer stars, boolean isFree) {
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.isFree = isFree;
    }

    public Room(Integer id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Integer getStars() {
        return stars;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public boolean isFree() {
        return isFree;
    }

    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        return (obj instanceof Room) && ((Room) obj).getId() != null && this.id.equals(((Room) obj).getId());
    }

    @Override
    public String toString() {
        return String.format("Room with id:%d, status:%s, capacity: %d, stars: %d, price: %.2f, is free:%s",
                id, status.toString(), capacity, stars, price, isFree);
    }
}

