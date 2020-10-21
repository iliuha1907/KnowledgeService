package com.senla.training.hoteladmin.controller.security;

import com.senla.training.hoteladmin.dto.AuthenticationDto;
import com.senla.training.hoteladmin.dto.MessageDto;
import com.senla.training.hoteladmin.dto.TokenDto;
import com.senla.training.hoteladmin.dto.mapper.TokenDtoMapper;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import com.senla.training.hoteladmin.model.user.User;
import com.senla.training.hoteladmin.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/authentication")
@RestController
public class AuthenticationController {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private TokenDtoMapper tokenDtoMapper;

    @PostMapping("/login")
    public TokenDto login(@RequestBody AuthenticationDto authentication) {
        String login = authentication.getLogin();
        String password = authentication.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login,
                    password));
            User user = userService.getUserByLogin(login);
            String token = tokenProvider.createToken(login, user.getRole().getType().name());
            return tokenDtoMapper.toDto(login, token);
        } catch (AuthenticationException ex) {
            LOGGER.error("Error at authentication: Invalid data");
            throw new IncorrectWorkException("Error at authentication: Invalid data");
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public MessageDto logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        return new MessageDto("Successfull logout");
    }
}
