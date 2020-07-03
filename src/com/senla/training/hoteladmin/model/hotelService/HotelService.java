package com.senla.training.hotelAdmin.model.hotelService;

import com.senla.training.hotelAdmin.model.client.Client;
import com.senla.training.hotelAdmin.util.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HotelService implements Serializable {
    private Integer id;
    private BigDecimal price;
    private HotelServiceType type;
    private Client client;
    private Date date;

    public HotelService() {
    }

    public HotelService(Integer id, BigDecimal price, HotelServiceType type, Client client, Date date) {
        this.id = id;
        this.price = price;
        this.type = type;
        this.client = client;
        this.date = date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Client getClient() {
        return client;
    }

    public Date getDate() {
        return date;
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
        return String.format("service %s, id:%d, price: %.2f, date: %s for client %s %s", type, id, price,
                DateUtil.getString(date), client.getFirstName(), client.getLastName());
    }
}

