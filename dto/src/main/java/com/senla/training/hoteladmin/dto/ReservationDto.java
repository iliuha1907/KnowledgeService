package com.senla.training.hoteladmin.dto;

import java.sql.Date;

public class ReservationDto extends AbstractDto {

    private Date arrivalDate;
    private Date departure;
    private Integer isActive;
    private Integer roomId;
    private Integer residentId;

    public ReservationDto() {
        super(0);
    }

    public ReservationDto(Integer id, Date arrivalDate, Date departure,
                          Integer isActive, Integer roomId, Integer residentId) {
        super(id);
        this.arrivalDate = arrivalDate;
        this.departure = departure;
        this.isActive = isActive;
        this.roomId = roomId;
        this.residentId = residentId;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public Date getDeparture() {
        return departure;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Integer getResidentId() {
        return residentId;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public void setResidentId(Integer residentId) {
        this.residentId = residentId;
    }
}
