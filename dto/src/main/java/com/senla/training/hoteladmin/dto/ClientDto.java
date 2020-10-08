package com.senla.training.hoteladmin.dto;

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
}
