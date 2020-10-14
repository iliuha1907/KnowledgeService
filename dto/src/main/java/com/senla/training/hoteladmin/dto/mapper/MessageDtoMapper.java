package com.senla.training.hoteladmin.dto.mapper;

import com.senla.training.hoteladmin.dto.MessageDto;
import org.springframework.stereotype.Component;

@Component
public class MessageDtoMapper {

    public MessageDto toDto(String data) {
        return new MessageDto(data);
    }
}
