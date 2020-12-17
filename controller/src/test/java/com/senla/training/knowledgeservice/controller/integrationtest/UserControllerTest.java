package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.UserSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.UserController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.user.UserDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.user.UserMapper;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class UserControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<User> users;
    private static List<UserDto> userDtos;
    private final UserDto userDtoForAll = new UserDto();
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserController userController;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    @BeforeAll
    public static void setUp() {
        UserProfile profile = new UserProfile(1, "Kyle", "Walker", new Date());
        User userOne = new User(1, "login", "password",
                RoleType.ROLE_USER, profile);
        User userTwo = new User(2, "login2", "password2",
                RoleType.ROLE_USER, profile);
        users = Arrays.asList(userOne, userTwo);
        userDtos = Arrays.asList(new UserDto(), new UserDto());
    }

    @Test
    void UserController_findUserById() {
        User user = users.get(0);
        Integer id = user.getId();

        Mockito.doReturn(userDtoForAll).when(userMapper).toDto(user);
        Mockito.doReturn(user).when(userDao).findById(id);
        Assertions.assertEquals(userDtoForAll, userController.findUserById(id));
    }

    @Test
    void UserController_findUserById_noUser() {
        String message = "Error at finding user by id: no such user";
        Integer id = 0;

        Mockito.doReturn(null).when(userDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.findUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_findUserById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> userController.findUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void UserServiceImpl_findUsers() {
        Mockito.doReturn(users).when(userDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Mockito.doReturn(userDtos).when(userMapper).listToDto(users);
        Assertions.assertIterableEquals(userDtos, userController
                .findUsers(UserSortCriterion.NATURAL, "login", null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TopicController_findTopics_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> userController.findUsers(UserSortCriterion.NATURAL, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserController_updateUser() {
        User user = users.get(0);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully updated user");
        Assertions.assertEquals(messageDtoForAll, userController.updateUser(userDtoForAll, id));
    }

    @Test
    void UserController_updateUser_loginIsNull() {
        String message = "Error at updating user: login is null";
        User user = new User(1, null, "", RoleType.ROLE_USER, new UserProfile());
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_profileIsNull() {
        String message = "Error at updating user: profile is null";
        User user = new User(1, "", "", RoleType.ROLE_USER, null);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_firstNameIsNull() {
        String message = "Error at updating user: first name is null";
        UserProfile profile = new UserProfile(1, null, "", new Date());
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_lastNameIsNull() {
        String message = "Error at updating user: last name is null";
        UserProfile profile = new UserProfile(1, "", null, new Date());
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_birthDateIsNull() {
        String message = "Error at updating user: birth date is null";
        UserProfile profile = new UserProfile(1, "", "", null);
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_wrongPassword() {
        String message = "Error at updating user: password should have length";
        UserProfile profile = new UserProfile(1, "", "", new Date());
        User user = new User(1, "login", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_exceptionAtExtractUser() {
        String message = "Error at extracting user";
        User user = users.get(0);

        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll,
                        user.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_roleIsNullWhenAdmin() {
        String message = "Error at updating user: role type is null";
        UserProfile profile = new UserProfile(1, "Name", "name", new Date());
        User user = new User(1, "log", "pas", null, profile);
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll,
                        user.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_wrongRole() {
        String message = "Error at updating user: unknown role of current user";
        User user = users.get(0);
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), user.getId(), null);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll,
                        user.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_noRights() {
        String message = "Error at updating user: not admin can not update another user";
        User user = users.get(0);
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), user.getId(), RoleType.ROLE_USER);

        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, -1));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_notUniqueUser() {
        String message = "Error at updating user: such login exists";
        User user = users.get(0);
        User existing = users.get(1);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(existing).when(userDao).findById(id);
        Mockito.doReturn(existing).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_noUser() {
        String message = "Error at updating user: no such user";
        UserProfile profile = new UserProfile(1, "", "", new Date());
        User user = new User(1, "log", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(null).when(userDao).findById(id);
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }


    @Test
    void UserController_updateUser_daoExceptionAtFindUser() {
        User user = users.get(1);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(id);
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserController_updateUser_daoExceptionAtFindByLogin() {
        User user = users.get(1);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(users.get(0)).when(userDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserController_updateUser_exceptionAtEncode() {
        String message = "Error at updating";
        User user = users.get(1);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        Mockito.doThrow(new IllegalArgumentException(message))
                .when(passwordEncoder).encode(user.getPassword());
        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_updateUser_daoExceptionAtUpdate() {
        User user = users.get(1);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userMapper).toEntity(userDtoForAll);
        Mockito.doReturn(user.getPassword()).when(passwordEncoder).encode(user.getPassword());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(userDao).update(user);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userController.updateUser(userDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserController_deleteUserById() {
        User user = users.get(0);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(messageDtoForAll).when(messageDtoMapper).toDto("Successfully " +
                "deleted user");
        Assertions.assertEquals(messageDtoForAll, userController.deleteUserById(id));
    }

    @Test
    void UserController_deleteUserById_noUser() {
        String message = "Error at deleting user by id: no such user";
        Integer id = 0;

        Mockito.doReturn(null).when(userDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userController.deleteUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserController_deleteUserById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> userController.deleteUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserController_deleteUserById_daoExceptionAtDelete() {
        User user = users.get(1);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(userDao).delete(user);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userController.deleteUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
