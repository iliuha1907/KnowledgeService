package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.controller.controller.AuthenticationController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.controller.security.TokenProvider;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.user.UserDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.user.UserMapper;
import com.senla.training.knowledgeservice.dto.security.TokenDto;
import com.senla.training.knowledgeservice.dto.security.TokenDtoMapper;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class AuthenticationControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private TokenDtoMapper tokenDtoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;

    @Test
    void AuthenticationController_register() {
        UserProfile profile = new UserProfile(1,"","",new Date());
        User user = new User(1,"login","password", RoleType.ROLE_USER, profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("User successfully registered");
        Assertions.assertEquals(messageDtoForAll, authenticationController.register(userDto));
    }

    @Test
    void AuthenticationController_register_loginIsNull() {
        String message = "Error at adding user: login is null";
        User user = new User(1,null,"", RoleType.ROLE_USER, new UserProfile());
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_profileIsNull() {
        String message = "Error at adding user: profile is null";
        User user = new User(1,"login","", RoleType.ROLE_USER, null);
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_firstNameIsNull() {
        String message = "Error at adding user: first name is null";
        UserProfile profile = new UserProfile(1,null,"",new Date());
        User user = new User(1,"","",RoleType.ROLE_USER,profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_lastNameIsNull() {
        String message = "Error at adding user: last name is null";
        UserProfile profile = new UserProfile(1,"Name",null,new Date());
        User user = new User(1,"","",RoleType.ROLE_USER,profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_birthDateIsNull() {
        String message = "Error at adding user: birth date is null";
        UserProfile profile = new UserProfile(1,"","",null);
        User user = new User(1,"","",RoleType.ROLE_USER,profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_passwordIsNull() {
        String message = "Error at adding user: password is null";
        UserProfile profile = new UserProfile(1,"","",new Date());
        User user = new User(1,"",null,RoleType.ROLE_USER,profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_wrongPassword() {
        String message = "Error at adding user: password should have length";
        UserProfile profile = new UserProfile(1,"","",new Date());
        User user = new User(1,"login","", RoleType.ROLE_USER, profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_notUniqueUser() {
        String message = "Error at adding user: such login exists";
        UserProfile profile = new UserProfile(1,"","",new Date());
        User user = new User(1,"login","pass", RoleType.ROLE_USER, profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(user).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_daoExceptionAtFindByLogin() {
        UserProfile profile = new UserProfile(1,"","",new Date());
        User user = new User(1,"login","pass", RoleType.ROLE_USER, profile);
        UserDto userDto = new UserDto();

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void AuthenticationController_register_exceptionAtEncode() {
        String message = "Error at registration";
        UserProfile profile = new UserProfile(1,"","",new Date());
        User user = new User(2,"wrong","wrong", RoleType.ROLE_USER, profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        Mockito.doThrow(new IllegalArgumentException(message))
                .when(passwordEncoder).encode(user.getPassword());
        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_register_daoExceptionAtAdd() {
        UserProfile profile = new UserProfile(1,"","",new Date());
        User user = new User(2,"login","pass", RoleType.ROLE_USER, profile);
        UserDto userDto = new UserDto();

        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDto);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).add(user);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> authenticationController.register(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void AuthenticationController_login() {
        User user = new User(1,"login","pass", RoleType.ROLE_USER,
                new UserProfile());
        String login = user.getLogin();
        String token = "example";
        UserDto userDto = new UserDto();
        TokenDto tokenDto = new TokenDto();
        userDto.setLogin(login);

        Mockito.doReturn(token).when(tokenProvider).createToken(login);
        Mockito.doReturn(user).when(userDao).findUserByLogin(login);
        Mockito.doReturn(tokenDto).when(tokenDtoMapper).toDto(login, token);
        Assertions.assertEquals(tokenDto, authenticationController.login(userDto));
    }

    @Test
    void AuthenticationController_login_noUser() {
        String message = "Error at authenticating user: no such user";
        User user = new User(1,"login","pass", RoleType.ROLE_USER,
                new UserProfile());
        String login = user.getLogin();
        UserDto userDto = new UserDto();
        userDto.setLogin(login);

        Mockito.doReturn(null).when(userDao).findUserByLogin(login);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> authenticationController.login(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_login_daoExceptionAtFind() {
        String message = "Error at find user";
        User user = new User(1,"login","pass", RoleType.ROLE_USER,
                new UserProfile());
        String login = user.getLogin();
        UserDto userDto = new UserDto();
        userDto.setLogin(login);

        Mockito.doReturn(user).when(userDao).findUserByLogin(login);
        Mockito.doThrow(new IllegalStateException(message)).when(userDao).findUserByLogin(login);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> authenticationController.login(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationController_login_authenticationException() {
        String message = "Error at authenticating user";
        User user = new User(1,"login","pass", RoleType.ROLE_USER,
                new UserProfile());
        String login = user.getLogin();
        UserDto userDto = new UserDto();
        userDto.setLogin(login);

        Mockito.doReturn(user).when(userDao).findUserByLogin(login);
        Mockito.doThrow(new BadCredentialsException(message)).when(authenticationManager)
                .authenticate(Matchers.any(UsernamePasswordAuthenticationToken.class));
        BadCredentialsException thrown = Assertions.assertThrows(
                BadCredentialsException.class, () -> authenticationController.login(userDto));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
