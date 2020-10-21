package com.senla.training.hoteladmin.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.hoteladmin.dto.ErrorMessageDto;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenFilter extends GenericFilterBean {

    private static final Logger LOGGER = LogManager.getLogger(TokenFilter.class);
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        try {
            String token = tokenProvider.getToken((HttpServletRequest) servletRequest);
            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (IncorrectWorkException ex) {
            sendError(ex, (HttpServletResponse) servletResponse);
            return;
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception ex) {
            sendError(ex, (HttpServletResponse) servletResponse);
        }
    }

    private void sendError(Exception ex, HttpServletResponse servletResponse) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto("IncorrectWorkException", ex.getMessage());
        servletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        try {
            String message = objectMapper.writeValueAsString(errorMessageDto);
            servletResponse.getWriter().write(message);
        } catch (Exception e) {
            LOGGER.error("Error at checking token: " + e.getMessage());
            throw new IncorrectWorkException(ex.getMessage());
        }
    }
}
