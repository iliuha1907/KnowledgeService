package com.senla.training.hoteladmin.model.visit;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "visits", schema = "hoteladmin_huzei")
public class Visit implements Serializable {

    private Integer id;
    private Client client;
    private HotelService service;
    private Date date;
    private Integer isActive;

    public Visit() {
    }

    public Visit(final Client client, final HotelService service) {
        this.client = client;
        this.service = service;
    }

    public Visit(final Client client, final HotelService service, final Date date,
                 final boolean isActive) {
        this.client = client;
        this.service = service;
        this.date = date;
        if (isActive) {
            this.isActive = 1;
        } else {
            this.isActive = 0;
        }
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    public Client getClient() {
        return client;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_service_id", referencedColumnName = "id", nullable = false)
    public HotelService getService() {
        return service;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    @Basic
    @Column(name = "is_active", nullable = false)
    public Integer getIsActive() {
        return isActive;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setClient(final Client client) {
        this.client = client;
    }

    public void setService(final HotelService service) {
        this.service = service;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setIsActive(final Integer isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Visit visit = (Visit) o;
        return Objects.equals(id, visit.id)
                && Objects.equals(client, visit.client)
                && Objects.equals(service, visit.service)
                && Objects.equals(date, visit.date)
                && Objects.equals(isActive, visit.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, service, date, isActive);
    }

    @Override
    public String toString() {
        return "Visit{" + "id: " + id + ", client: " + client
                + ", hotelService: " + service + ", date: " + date
                + ", isActive: " + isActive + '}';
    }
}
