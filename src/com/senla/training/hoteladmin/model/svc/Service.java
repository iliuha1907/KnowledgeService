package com.senla.training.hoteladmin.model.svc;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Service {
    private Integer id;
    private BigDecimal price;
    private ServiceType type;
    private Client client;
    private Date date;

    public Service(Integer id, BigDecimal price, ServiceType type, Client client, Date date) {
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

    public void setType(ServiceType type) {
        this.type = type;
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

    public ServiceType getType() {
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
        return (obj instanceof Service) && this.id.equals(((Service) obj).getId());
    }

    @Override
    public int hashCode() {
        return id;
    }



    @Override
    public String toString() {
        return String.format("service %s, id:%d, price: %.2f, date: %s for client %s %s", type,id, price,
                DateUtil.getStr(date), client.getFirstName(), client.getLastName());
    }
}

