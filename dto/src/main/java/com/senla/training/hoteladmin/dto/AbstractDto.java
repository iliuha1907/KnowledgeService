package com.senla.training.hoteladmin.dto;

public class AbstractDto {

    private Integer id;

    public AbstractDto(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
