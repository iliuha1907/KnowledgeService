package com.senla.training.hoteladmin.dto.mapper;

import com.senla.training.hoteladmin.dto.HotelServiceDto;
import com.senla.training.hoteladmin.model.hotelservice.HotelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotelServiceMapper {

    @Autowired
    private ModelMapper mapper;

    public HotelService toEntity(HotelServiceDto dto) {
        if (dto == null) {
            return null;
        }
        return mapper.map(dto, HotelService.class);
    }

    public List<HotelServiceDto> listToDto(List<HotelService> hotelServices) {
        List<HotelServiceDto> hotelServiceDtos = new ArrayList<>();
        hotelServices.forEach(hotelService -> {
            if (hotelService != null) {
                hotelServiceDtos.add(mapper.map(hotelService, HotelServiceDto.class));
            }
        });
        return hotelServiceDtos;
    }
}
