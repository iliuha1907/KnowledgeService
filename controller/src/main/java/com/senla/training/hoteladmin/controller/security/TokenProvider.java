package com.senla.training.hoteladmin.controller.security;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.model.user.User;
import com.senla.training.hoteladmin.service.user.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

@Component
public class TokenProvider {

    @Value("${authorization.header:Authorization}")
    private String authorizationHeader;
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

    public String createToken(String login, String role) {
        Claims claims = Jwts.claims().setSubject(login);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }
    }

    public Authentication getAuthentication(String token) {
        User user = userService.getUserByLogin(getLogin(token));
        if (user == null) {
            throw new IncorrectWorkException("Could not find user to autheticate");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(), new HashSet<GrantedAuthority>(Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getType().name()))));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }

    private String getLogin(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }
}
