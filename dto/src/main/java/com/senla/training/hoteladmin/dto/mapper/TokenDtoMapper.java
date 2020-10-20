package com.senla.training.hoteladmin.dto.mapper;

import com.senla.training.hoteladmin.dto.TokenDto;
import org.springframework.stereotype.Component;

@Component
public class TokenDtoMapper {

    public TokenDto toDto(String login, String token) {
        return new TokenDto(login, token);
    }
}
