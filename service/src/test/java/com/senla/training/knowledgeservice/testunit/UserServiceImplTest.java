package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.UserSortCriterion;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import com.senla.training.knowledgeservice.service.service.user.UserService;
import com.senla.training.knowledgeservice.testunit.config.ServiceTestConfigurator;
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
@ContextConfiguration(classes = ServiceTestConfigurator.class)
public class UserServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<User> users;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
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
    }

    @Test
    void UserServiceImpl_addUser() {
        User user = users.get(0);

        Mockito.doReturn(user.getPassword()).when(passwordEncoder).encode(user.getPassword());
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Assertions.assertDoesNotThrow(() -> userService.addUser(user));
    }

    @Test
    void UserServiceImpl_addUser_notUniqueLogin() {
        String message = "Error at adding user: such login exists";
        User user = users.get(0);

        Mockito.doReturn(user).when(userDao).findUserByLogin(user.getLogin());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_wrongPassword() {
        String message = "Error at adding user: password should have length";
        UserProfile profile = new UserProfile(1, "Kyle", "Walker", new Date());
        User user = new User(1, "login", "", RoleType.ROLE_USER, profile);

        Mockito.doReturn(user).when(userDao).findUserByLogin(user.getLogin());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_loginIsNull() {
        String message = "Error at adding user: login is null";
        User user = new User(1, null, "", RoleType.ROLE_USER, new UserProfile());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_profileIsNull() {
        String message = "Error at adding user: profile is null";
        User user = new User(1, "", "", RoleType.ROLE_USER, null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_firstNameIsNull() {
        String message = "Error at adding user: first name is null";
        UserProfile profile = new UserProfile(1, null, "", new Date());
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_lastNameIsNull() {
        String message = "Error at adding user: last name is null";
        UserProfile profile = new UserProfile(1, "", null, new Date());
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_birthDateIsNull() {
        String message = "Error at adding user: birth date is null";
        UserProfile profile = new UserProfile(1, "", "", null);
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_passwordIsNull() {
        String message = "Error at adding user: password is null";
        UserProfile profile = new UserProfile(1, "", "", new Date());
        User user = new User(1, "", null, RoleType.ROLE_USER, profile);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_exceptionAtEncode() {
        String message = "Error at adding";
        User user = users.get(1);

        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doThrow(new IllegalArgumentException(message))
                .when(passwordEncoder).encode(user.getPassword());
        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_addUser_daoExceptionAtAdd() {
        User user = users.get(1);

        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user.getPassword()).when(passwordEncoder).encode(user.getPassword());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).add(user);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserServiceImpl_addUser_daoExceptionAtFindByLogin() {
        User user = users.get(0);

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findUserByLogin(user.getLogin());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userService.addUser(user));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserServiceImpl_findUserById() {
        User user = users.get(0);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Assertions.assertEquals(user, userService.findUserById(id));
    }

    @Test
    void UserServiceImpl_findUserById_noUser() {
        String message = "Error at finding user by id: no such user";
        Integer id = 0;

        Mockito.doReturn(null).when(userDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.findUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_findUserById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> userService.findUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserServiceImpl_findUserByLogin() {
        User user = users.get(0);
        String login = user.getLogin();

        Mockito.doReturn(user).when(userDao).findUserByLogin(login);
        Assertions.assertEquals(user, userService.findUserByLogin(login));
    }

    @Test
    void UserServiceImpl_findUserByLogin_noUser() {
        String message = "Error at finding user by login: no such user";
        String login = "wrong";

        Mockito.doReturn(null).when(userDao).findUserByLogin(login);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.findUserByLogin(login));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_findUserByLogin_daoExceptionAtFindUserByLogin() {
        String login = "mail";

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findUserByLogin(login);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userService.findUserByLogin(login));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void UserServiceImpl_findUsers() {
        Mockito.doReturn(users).when(userDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> userService
                .findUsers(UserSortCriterion.NATURAL, "login", null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TopicServiceImpl_findTopics_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> userService.findUsers(UserSortCriterion.NATURAL, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserServiceImpl_updateUser() {
        User user = users.get(0);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user.getPassword()).when(passwordEncoder).encode(user.getPassword());
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doReturn(user).when(userDao).findById(id);
        Assertions.assertDoesNotThrow(() -> userService.updateUser(user, id));
    }

    @Test
    void UserServiceImpl_updateUser_loginIsNull() {
        String message = "Error at updating user: login is null";
        User user = new User(1, null, "", RoleType.ROLE_USER, new UserProfile());
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_profileIsNull() {
        String message = "Error at updating user: profile is null";
        User user = new User(1, "", "", RoleType.ROLE_USER, null);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_firstNameIsNull() {
        String message = "Error at updating user: first name is null";
        UserProfile profile = new UserProfile(1, null, "", new Date());
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_lastNameIsNull() {
        String message = "Error at updating user: last name is null";
        UserProfile profile = new UserProfile(1, "", null, new Date());
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_birthDateIsNull() {
        String message = "Error at updating user: birth date is null";
        UserProfile profile = new UserProfile(1, "", "", null);
        User user = new User(1, "", "", RoleType.ROLE_USER, profile);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_wrongPassword() {
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
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_exceptionAtExtractUser() {
        String message = "Error at extracting user";
        User user = users.get(0);

        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, user.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_roleIsNullWhenAdmin() {
        String message = "Error at updating user: role type is null";
        UserProfile profile = new UserProfile(1, "Name", "name", new Date());
        User user = new User(1, "log", "pas", null, profile);
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, user.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_wrongRole() {
        String message = "Error at updating user: unknown role of current user";
        User user = users.get(0);
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), user.getId(), null);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, user.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_noRights() {
        String message = "Error at updating user: not admin can not update another user";
        User user = users.get(0);
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), user.getId(), RoleType.ROLE_USER);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, -1));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_notUniqueLogin() {
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
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_noUser() {
        String message = "Error at updating user: no such user";
        User user = users.get(0);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(null).when(userDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_daoExceptionAtFindUser() {
        User user = users.get(0);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserServiceImpl_updateUser_daoExceptionAtFindByLogin() {
        User user = users.get(0);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(users.get(1)).when(userDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findUserByLogin(user.getLogin());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserServiceImpl_updateUser_exceptionAtEncode() {
        String message = "Error at updating";
        User user = users.get(1);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(null).when(userDao).findUserByLogin(user.getLogin());
        Mockito.doThrow(new IllegalArgumentException(message))
                .when(passwordEncoder).encode(user.getPassword());
        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_updateUser_daoExceptionAtUpdate() {
        User user = users.get(1);
        Integer id = user.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), id, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doReturn(user.getPassword()).when(passwordEncoder).encode(user.getPassword());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(userDao).update(user);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userService.updateUser(user, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void UserServiceImpl_deleteUserById() {
        User user = users.get(0);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Assertions.assertDoesNotThrow(() -> userService.deleteUserById(id));
    }

    @Test
    void UserServiceImpl_deleteUserById_noUser() {
        String message = "Error at deleting user by id: no such user";
        Integer id = 0;

        Mockito.doReturn(null).when(userDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> userService.deleteUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_deleteUserById_daoExceptionAtFindUser() {
        String message = "Error at deleting user by id: no such user";
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(message)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> userService.deleteUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void UserServiceImpl_deleteUserById_daoExceptionAtDelete() {
        User user = users.get(1);
        Integer id = user.getId();

        Mockito.doReturn(user).when(userDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(userDao).delete(user);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> userService.deleteUserById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

}
