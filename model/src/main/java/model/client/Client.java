package model.client;

public class Client {

    private Integer id;
    private String firstName;
    private String lastName;

    public Client(final Integer id, final String firstName, final String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client(final Integer id) {
        this.id = id;
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

