package com.senla.training.hoteladmin.dto;

import java.sql.Date;

public class VisitDto extends AbstractDto {

    private Integer clientId;
    private Integer serviceId;
    private Date date;
    private Integer isActive;

    public VisitDto() {
        super(0);
    }

    public VisitDto(Integer id, Integer clientId, Integer serviceId, Date date, Integer isActive) {
        super(id);
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.date = date;
        this.isActive = isActive;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}
