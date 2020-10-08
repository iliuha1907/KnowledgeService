package com.senla.training.hoteladmin.dto.mapper;

import com.senla.training.hoteladmin.dto.ReservationDto;
import com.senla.training.hoteladmin.model.reservation.Reservation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationMapper {

    @Autowired
    private ModelMapper mapper;

    public List<ReservationDto> listToDto(List<Reservation> reservations) {
        List<ReservationDto> reservationDtos = new ArrayList<>();
        reservations.forEach(reservation -> {
            if (reservation != null) {
                reservationDtos.add(mapper.map(reservation, ReservationDto.class));
            }
        });
        return reservationDtos;
    }

    public void setMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }
}
