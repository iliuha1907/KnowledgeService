package com.senla.training.hoteladmin.model.room;

import com.senla.training.hoteladmin.model.reservation.Reservation;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rooms", schema = "hoteladmin_huzei")
public class Room implements Serializable {

    private Integer id;
    private String status;
    private BigDecimal price;
    private Integer capacity;
    private Integer stars;
    private Integer isFree;
    private Set<Reservation> clientReservations = new HashSet<>();

    public Room() {
    }

    public Room(final Integer id) {
        this.id = id;
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

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 45)
    public String getStatus() {
        return status;
    }

    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    public BigDecimal getPrice() {
        return price;
    }

    @Basic
    @Column(name = "capacity", nullable = false)
    public Integer getCapacity() {
        return capacity;
    }

    @Basic
    @Column(name = "stars", nullable = false)
    public Integer getStars() {
        return stars;
    }

    @Basic
    @Column(name = "is_free", nullable = false)
    public Integer getIsFree() {
        return isFree;
    }

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    public Set<Reservation> getClientReservations() {
        return clientReservations;
    }

    public void setId(final Integer id) {
        this.id = id;
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
                && Objects.equals(status, room.status)
                && Objects.equals(price, room.price)
                && Objects.equals(capacity, room.capacity)
                && Objects.equals(stars, room.stars)
                && Objects.equals(isFree, room.isFree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, price, capacity, stars, isFree);
    }

    @Override
    public String toString() {
        return "Room{" + "id: " + id + ", status: " + status
                + ", price: " + price + ", capacity: " + capacity
                + ", stars: " + stars + ", isFree: " + (isFree == 1) + '}';
    }
}
