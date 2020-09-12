package com.senla.training.hoteladmin.model.client;

import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.visit.Visit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clients", schema = "hoteladmin_huzei")
public class Client implements Serializable {

    private Integer id;
    private String name;
    private String lastName;
    private Set<Reservation> clientReservations = new HashSet<>();
    private Set<Visit> clientVisits = new HashSet<>();

    public Client() {
    }

    public Client(final Integer id) {
        this.id = id;
    }

    public Client(final String name, final String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Client(final Integer clientId, final String name, final String lastName) {
        this.id = clientId;
        this.name = name;
        this.lastName = lastName;
    }

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    public String getLastName() {
        return lastName;
    }

    @OneToMany(mappedBy = "resident", fetch = FetchType.LAZY)
    public Set<Reservation> getClientReservations() {
        return clientReservations;
    }

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    public Set<Visit> getClientVisits() {
        return clientVisits;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setClientReservations(final Set<Reservation> clientReservations) {
        this.clientReservations = clientReservations;
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
        Client client = (Client) o;
        return Objects.equals(id, client.id)
                && Objects.equals(name, client.name)
                && Objects.equals(lastName, client.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName);
    }

    @Override
    public String toString() {
        return "Client{" + "id: " + id + ", name: " + name
                + ", lastName: " + lastName + '}';
    }
}
