package com.senla.training.hoteladmin.model.hotelservice;

import com.senla.training.hoteladmin.model.visit.Visit;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "hotel_services", schema = "hoteladmin_huzei")
public class HotelService implements Serializable {

    private Integer id;
    private BigDecimal price;
    private String type;
    private Set<Visit> clientVisits = new HashSet<>();

    public HotelService() {
    }

    public HotelService(final Integer id) {
        this.id = id;
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

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    public BigDecimal getPrice() {
        return price;
    }

    @Basic
    @Column(name = "type", nullable = false, length = 20)
    public String getType() {
        return type;
    }

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    public Set<Visit> getClientVisits() {
        return clientVisits;
    }

    public void setId(final Integer id) {
        this.id = id;
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
