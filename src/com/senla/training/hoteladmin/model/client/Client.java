package com.senla.training.hoteladmin.model.client;

public class Client {
    private Integer id;
    private String firstName;
    private String lastName;

    public Client(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format("Client %s %s, id: %d", firstName, lastName, id);
    }
}

