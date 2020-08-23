package com.senla.training.hoteladmin.model.hotelservice;

import java.math.BigDecimal;

public class HotelService {
    private Integer id;
    private BigDecimal price;
    private HotelServiceType type;

    public HotelService(BigDecimal price, HotelServiceType type) {
        this.price = price;
        this.type = type;
    }

    public HotelService(Integer id, BigDecimal price, HotelServiceType type) {
        this.id = id;
        this.price = price;
        this.type = type;
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

    public BigDecimal getPrice() {
        return price;
    }

    public HotelServiceType getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (id == null) {
            return false;
        }
        return (obj instanceof HotelService) && this.id.equals(((HotelService) obj).getId());
    }

    @Override
    public String toString() {
        return String.format("service %s, id:%d, price: %.2f", type, id, price);
    }
}

