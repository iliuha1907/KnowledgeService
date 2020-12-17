package com.senla.training.knowledgeservice.dto.mapper.message;

import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class MessageDtoMapper {

    @Nonnull
    public MessageDto toDto(@Nullable String data) {
        return new MessageDto(data);
    }
}
