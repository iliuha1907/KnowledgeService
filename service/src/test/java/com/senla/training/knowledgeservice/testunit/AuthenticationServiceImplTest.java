package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.service.authentication.AuthenticationService;
import com.senla.training.knowledgeservice.testunit.config.ServiceTestConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfigurator.class)
public class AuthenticationServiceImplTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDao userDao;

    @Test
    void AuthenticationServiceImpl_findAndAuthenticateUser() {
        User user = new User(1, "login", "password", RoleType.ROLE_USER,
                new UserProfile());
        String login = user.getLogin();
        String password = user.getPassword();

        Mockito.doReturn(user).when(userDao).findUserByLogin(login);
        Assertions.assertDoesNotThrow(() -> authenticationService.authenticateUser(
                login, password));
    }

    @Test
    void AuthenticationServiceImpl_findAndAuthenticateUser_noUser() {
        String message = "Error at authenticating user: no such user";
        String login = "wrong";
        String password = "password";

        Mockito.doReturn(null).when(userDao).findUserByLogin(login);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> authenticationService.authenticateUser(login, password));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationServiceImpl_findAndAuthenticateUser_wrongData() {
        String message = "Error at authenticating user";
        User user = new User();
        String login = "mail";
        String password = "password";

        Mockito.doReturn(user).when(userDao).findUserByLogin(login);
        Mockito.doThrow(new BadCredentialsException(message)).when(authenticationManager)
                .authenticate(Matchers.any(UsernamePasswordAuthenticationToken.class));
        BadCredentialsException thrown = Assertions.assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.authenticateUser(login, password));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void AuthenticationServiceImpl_findAndAuthenticateUser_daoExceptionAtFind() {
        String message = "Error at find user";
        String login = "mail";
        String password = "password";

        Mockito.doThrow(new IllegalStateException(message)).when(userDao).findUserByLogin(login);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> authenticationService.authenticateUser(login, password));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }
}
