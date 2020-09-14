package com.senla.training.hoteladmin.model.hotelservice;

import com.senla.training.hoteladmin.model.AbstractEntity;
import com.senla.training.hoteladmin.model.visit.Visit;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "hotel_services")
public class HotelService extends AbstractEntity {

    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    private BigDecimal price;
    @Basic
    @Column(name = "type", nullable = false, length = 20)
    private String type;
    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private Set<Visit> clientVisits = new HashSet<>();

    public HotelService() {
    }

    public HotelService(final Integer id) {
        super(id);
    }

    public HotelService(final BigDecimal price, final HotelServiceType type) {
        this.price = price;
        this.type = type.toString();
    }

    public HotelService(final Integer id, final BigDecimal price, final HotelServiceType type) {
        this.id = id;
        this.price = price;
        this.type = type.toString();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public Set<Visit> getClientVisits() {
        return clientVisits;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setClientVisits(final Set<Visit> clientVisits) {
        this.clientVisits = clientVisits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HotelService that = (HotelService) o;
        return Objects.equals(id, that.id)
                && Objects.equals(price, that.price)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, type);
    }

    @Override
    public String toString() {
        return "HotelService{" + "id: " + id + ", price: " + price
                + ", type: " + type + '}';
    }
}
