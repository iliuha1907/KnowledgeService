package com.senla.training.hoteladmin.model.reservation;

import com.senla.training.hoteladmin.model.client.Client;
import com.senla.training.hoteladmin.model.room.Room;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "reservations", schema = "hoteladmin_huzei")
public class Reservation implements Serializable {

    private Integer id;
    private Date arrivalDate;
    private Date departure;
    private Integer isActive;
    private Room room;
    private Client resident;

    public Reservation() {
    }

    public Reservation(final Client client, final Room room, final Date arrivalDate, final Date departure) {
        this.resident = client;
        this.room = room;
        this.arrivalDate = arrivalDate;
        this.departure = departure;
    }

    public Reservation(final Room room, final Client client, final Date arrivalDate, final Date departure,
                       Integer isActive) {
        this.resident = client;
        this.room = room;
        this.arrivalDate = arrivalDate;
        this.departure = departure;
        this.isActive = isActive;
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "arrival_date", nullable = false)
    public Date getArrivalDate() {
        return arrivalDate;
    }

    @Basic
    @Column(name = "departure_date", nullable = false)
    public Date getDeparture() {
        return departure;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "resident_id", referencedColumnName = "id", nullable = false)
    public Client getResident() {
        return resident;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    public Room getRoom() {
        return room;
    }

    @Basic
    @Column(name = "is_active", nullable = false)
    public Integer getIsActive() {
        return isActive;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setArrivalDate(final Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDeparture(final Date departureDate) {
        this.departure = departureDate;
    }

    public void setResident(final Client resident) {
        this.resident = resident;
    }

    public void setRoom(final Room room) {
        this.room = room;
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
        Reservation that = (Reservation) o;
        return isActive == that.isActive
                && Objects.equals(id, that.id)
                && Objects.equals(arrivalDate, that.arrivalDate)
                && Objects.equals(departure, that.departure)
                && Objects.equals(room, that.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, arrivalDate, departure, isActive, room, resident);
    }

    @Override
    public String toString() {
        return "Reservation{" + "id: " + id + ", arrivalDate: " + arrivalDate
                + ", departureDate: " + departure + ", isActive: " + (isActive == 1)
                + ", room: " + room + ", resident: " + resident + '}';
    }
}
