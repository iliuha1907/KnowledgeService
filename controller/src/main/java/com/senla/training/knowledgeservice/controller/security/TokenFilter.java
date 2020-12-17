package com.senla.training.knowledgeservice.controller.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.exception.IncorrectWorkException;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = tokenProvider.getToken((HttpServletRequest) servletRequest);
            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (IncorrectWorkException | BusinessException ex) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new MessageDto(
                    ex.getMessage())));
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
