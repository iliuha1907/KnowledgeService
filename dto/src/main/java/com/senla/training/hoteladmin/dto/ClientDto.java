package com.senla.training.hoteladmin.dto;

import java.util.Objects;

public class ClientDto extends AbstractDto {

    private String name;
    private String lastName;

    public ClientDto() {
        super(0);
    }

    public ClientDto(Integer id, String name, String lastName) {
        super(id);
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ClientDto clientDto = (ClientDto) o;
        return Objects.equals(name, clientDto.name)
                && Objects.equals(lastName, clientDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, lastName);
    }
}
