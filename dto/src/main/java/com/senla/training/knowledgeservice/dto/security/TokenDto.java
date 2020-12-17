package com.senla.training.knowledgeservice.dto.security;

import java.util.Objects;

public class TokenDto {

    private String login;
    private String token;

    public TokenDto() {
    }

    public TokenDto(String login, String token) {
        this.login = login;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TokenDto tokenDto = (TokenDto) o;
        return Objects.equals(login, tokenDto.login)
                && Objects.equals(token, tokenDto.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, token);
    }

    @Override
    public String toString() {
        return "TokenDto{"
                + "login='" + login
                + ", token='" + token
                + '}';
    }
}
