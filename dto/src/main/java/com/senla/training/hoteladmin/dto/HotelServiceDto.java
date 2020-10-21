package com.senla.training.hoteladmin.dto;

import com.senla.training.hoteladmin.model.hotelservice.HotelServiceType;

import java.math.BigDecimal;

public class HotelServiceDto extends AbstractDto {

    private BigDecimal price;
    private HotelServiceType type;

    public HotelServiceDto() {
        super(null);
    }

    public HotelServiceDto(Integer id, BigDecimal price, HotelServiceType type) {
        super(id);
        this.price = price;
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public HotelServiceType getType() {
        return type;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setType(HotelServiceType type) {
        this.type = type;
    }
}
