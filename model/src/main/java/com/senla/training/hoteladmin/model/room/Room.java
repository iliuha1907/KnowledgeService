package com.senla.training.hoteladmin.model.room;

import com.senla.training.hoteladmin.model.AbstractEntity;
import com.senla.training.hoteladmin.model.reservation.Reservation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room extends AbstractEntity {

    @Basic
    @Column(name = "status", nullable = false, length = 45)
    private String status;
    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    private BigDecimal price;
    @Basic
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    @Basic
    @Column(name = "stars", nullable = false)
    private Integer stars;
    @Basic
    @Column(name = "is_free", nullable = false)
    private Integer isFree;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private Set<Reservation> clientReservations = new HashSet<>();

    public Room() {
    }

    public Room(final Integer id) {
        super(id);
    }

    public Room(final RoomStatus status, final BigDecimal price, final Integer capacity,
                final Integer stars) {
        this.status = status.toString();
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        this.isFree = 1;
    }

    public Room(final Integer id, final RoomStatus roomStatus, final BigDecimal price, final Integer capacity,
                final Integer stars, final boolean isFree) {
        this.id = id;
        this.status = roomStatus.toString();
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        if (isFree) {
            this.isFree = 1;
        } else {
            this.isFree = 0;
        }
    }

    public Room(final RoomStatus roomStatus, final BigDecimal price, final Integer capacity,
                final Integer stars, final boolean isFree) {
        this.status = roomStatus.toString();
        this.price = price;
        this.capacity = capacity;
        this.stars = stars;
        if (isFree) {
            this.isFree = 1;
        } else {
            this.isFree = 0;
        }
    }

    public String getStatus() {
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

    public Set<Reservation> getClientReservations() {
        return clientReservations;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setCapacity(final Integer capacity) {
        this.capacity = capacity;
    }

    public void setStars(final Integer stars) {
        this.stars = stars;
    }

    public void setIsFree(final Integer isFree) {
        this.isFree = isFree;
    }

    public void setClientReservations(final Set<Reservation> clientReservations) {
        this.clientReservations = clientReservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id)
                && Objects.equals(price, room.price)
                && Objects.equals(capacity, room.capacity)
                && Objects.equals(stars, room.stars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, capacity, stars);
    }

    @Override
    public String toString() {
        return "Room{" + "id: " + id + ", status: " + status
                + ", price: " + price + ", capacity: " + capacity
                + ", stars: " + stars + ", isFree: " + (isFree == 1) + '}';
    }
}
