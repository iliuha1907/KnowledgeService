package com.senla.training.hoteladmin.model.svc;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Service {
    private BigDecimal price;
    private ServiceType type;
    private Client client;
    private Date date;

    public Service(BigDecimal price, ServiceType type, Client client, Date date) {
        this.price = price;
        this.type = type;
        this.client = client;
        this.date = date;
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
    public String toString() {
        return String.format("service %s, price: %.2f, date: %s for client %s %s", type, price,
                DateUtil.getStr(date), client.getFirstName(), client.getLastName());
    }
}

