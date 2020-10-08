package com.senla.training.hoteladmin.dto.mapper;

import com.senla.training.hoteladmin.dto.VisitDto;
import com.senla.training.hoteladmin.model.visit.Visit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VisitMapper {

    @Autowired
    private ModelMapper mapper;

    public List<VisitDto> listToDto(List<Visit> rooms) {
        List<VisitDto> visitDtos = new ArrayList<>();
        rooms.forEach(visit -> {
            if (visit != null) {
                visitDtos.add(mapper.map(visit, VisitDto.class));
            }
        });
        return visitDtos;
    }
}
