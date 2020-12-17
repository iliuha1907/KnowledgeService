package com.senla.training.knowledgeservice.controller.security;

import com.senla.training.knowledgeservice.common.exception.IncorrectWorkException;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import com.senla.training.knowledgeservice.service.service.user.UserService;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class TokenProvider {

    private static final Logger LOGGER = LogManager.getLogger(TokenProvider.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_STRING = "Bearer ";
    @Value("${authorization.key:key}")
    private String key;
    @Value("${authorization.time:1000000}")
    private Integer validationTime;
    @Autowired
    private UserService userService;

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    public String createToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new IncorrectWorkException("Error at validating token");
        }
    }

    public Authentication getAuthentication(String token) {
        User user = userService.findUserByLogin(getLogin(token));
        RoleType roleType = user.getRoleType();

        UserDetails userDetails = new FullUserDetails(
                user.getLogin(), user.getPassword(), Collections.singletonList(
                        new SimpleGrantedAuthority(
                roleType.name())), user.getId(), roleType);
        return new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
    }

    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        int bearerLength = BEARER_STRING.length();
        if (token != null && token.startsWith(BEARER_STRING) && token.length() > bearerLength) {
            return token.substring(bearerLength);
        }
        return null;
    }

    private String getLogin(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody()
                .getSubject();
    }
}
