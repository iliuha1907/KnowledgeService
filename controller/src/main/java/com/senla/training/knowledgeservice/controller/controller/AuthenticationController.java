package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.controller.security.TokenProvider;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.user.UserDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.user.UserMapper;
import com.senla.training.knowledgeservice.dto.security.TokenDto;
import com.senla.training.knowledgeservice.dto.security.TokenDtoMapper;
import com.senla.training.knowledgeservice.service.service.authentication.AuthenticationService;
import com.senla.training.knowledgeservice.service.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Api(tags = SpringFoxConfig.AUTHENTICATION_CONTROLLER_TAG)
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private TokenDtoMapper tokenDtoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Registers user and responses with message, all users",
            response = MessageDto.class)
    @PostMapping("/registration")
    public MessageDto register(@ApiParam(value = "Defines data to register")
                               @RequestBody UserDto userDto) {
        userService.addUser(userMapper.toEntity(userDto));
        return messageDtoMapper.toDto("User successfully registered");
    }

    @ApiOperation(value = "Logs user in and responses with token and login, all users",
            response = TokenDto.class)
    @PostMapping("/login")
    public TokenDto login(@ApiParam(value = "Defines data to login")
                          @RequestBody UserDto userDto) {
        String login = userDto.getLogin();
        authenticationService.authenticateUser(login, userDto.getPassword());
        return tokenDtoMapper.toDto(login, tokenProvider.createToken(login));
    }

    @ApiOperation(value = "Logs user out and responses with message, all authenticated",
            response = MessageDto.class)
    @PostMapping("/logout")
    public MessageDto logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler =
                new SecurityContextLogoutHandler();
        securityContextLogoutHandler.setClearAuthentication(true);
        securityContextLogoutHandler.setInvalidateHttpSession(true);
        securityContextLogoutHandler.logout(request, response, null);
        return new MessageDto("Successfull logout");
    }
}
