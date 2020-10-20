package com.senla.training.hoteladmin.dto;

import com.senla.training.hoteladmin.model.room.RoomStatus;

import java.math.BigDecimal;

public class RoomDto extends AbstractDto {

    private RoomStatus status;
    private BigDecimal price;
    private Integer capacity;
    private Integer stars;
    private Integer isFree;

    public RoomDto() {
        super(null);
    }

    public RoomDto(Integer id, RoomStatus status, BigDecimal price, Integer capacity, Integer stars, Integer isFree) {
        super(id);
        this.status = status;
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.isFree = isFree;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getStars() {
        return stars;
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }
}
