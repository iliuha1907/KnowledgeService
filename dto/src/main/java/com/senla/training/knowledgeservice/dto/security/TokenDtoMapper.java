package com.senla.training.knowledgeservice.dto.security;

import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class TokenDtoMapper {

    @Nonnull
    public TokenDto toDto(@Nullable String login, @Nullable String token) {
        return new TokenDto(login, token);
    }
}
