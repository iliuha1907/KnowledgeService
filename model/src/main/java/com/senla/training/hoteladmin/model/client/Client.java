package com.senla.training.hoteladmin.model.client;

import com.senla.training.hoteladmin.model.AbstractEntity;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import com.senla.training.hoteladmin.model.visit.Visit;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client extends AbstractEntity {

    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    private String name;
    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    @OneToMany(mappedBy = "resident", fetch = FetchType.LAZY)
    private Set<Reservation> clientReservations = new HashSet<>();
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private Set<Visit> clientVisits = new HashSet<>();

    public Client() {
    }

    public Client(final Integer id) {
        super(id);
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

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Reservation> getClientReservations() {
        return clientReservations;
    }

    public Set<Visit> getClientVisits() {
        return clientVisits;
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
