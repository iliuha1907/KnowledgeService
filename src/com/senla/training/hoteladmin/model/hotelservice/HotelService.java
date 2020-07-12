package com.senla.training.hoteladmin.model.hotelservice;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.DateUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HotelService implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private BigDecimal price;
    private HotelServiceType type;
    private Client client;
    private Date date;

    public HotelService() {
    }

    public HotelService(BigDecimal price, HotelServiceType type, Client client, Date date) {
        this.price = price;
        this.type = type;
        this.client = client;
        this.date = date;
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
        return String.format("service %s, idspread:%d, price: %.2f, date: %s for client %s %s", type, id, price,
                DateUtil.getString(date), client.getFirstName(), client.getLastName());
    }
}

