package com.senla.training.knowledgeservice.service.service.user;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.UserSortCriterion;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.user.User_;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.service.EntityOperation;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void addUser(@Nonnull User user) {
        checkUserForNullsWithoutPasswordAndRole(user, EntityOperation.ADDING);
        if (user.getPassword() == null) {
            throw new BusinessException("Error at adding user: password is null");
        }
        if ("".equals(user.getPassword())) {
            throw new BusinessException("Error at adding user: password should "
                    + "have length");
        }
        if (userDao.findUserByLogin(user.getLogin()) != null) {
            throw new BusinessException("Error at adding user: such login exists");
        }
        user.setRoleType(RoleType.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(null);
        userDao.add(user);
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<User> findUsers(
            @Nonnull UserSortCriterion criterion,
            @Nullable String login,
            @Nullable RoleType type) {
        List<EqualQueryHandler<User, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<User, Y>> compareParameters = new ArrayList<>();
        formEqualHandlersList(equalParameters, login, type);
        return userDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    @Nonnull
    public User findUserById(@Nonnull Integer id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new BusinessException("Error at finding user by id: no such user");
        }
        return user;
    }

    @Override
    @Transactional
    @Nonnull
    public User findUserByLogin(@Nullable String login) {
        User user = userDao.findUserByLogin(login);
        if (user == null) {
            throw new BusinessException("Error at finding user by login:"
                    + " no such user");
        }
        return user;
    }

    @Override
    @Transactional
    public void updateUser(@Nonnull User user, @Nonnull Integer id) {
        checkUserForNullsWithoutPasswordAndRole(user, EntityOperation.UPDATING);
        handleUserByAuthorities(id, user,
                currentUserProvider.extractFullUserDetailsFromCurrentUser());
        String login = user.getLogin();
        User existing = userDao.findById(id);
        if (existing == null) {
            throw new BusinessException("Error at updating user: no such user");
        }
        if (!existing.getLogin().equals(login) && userDao.findUserByLogin(login)
                != null) {
            throw new BusinessException("Error at updating user: such login exists");
        }
        String newPassword = user.getPassword();
        if (newPassword != null) {
            if ("".equals(newPassword)) {
                throw new BusinessException("Error at updating user: password"
                        + " should have length");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        } else {
            user.setPassword(existing.getPassword());
        }
        user.setId(id);
        userDao.update(user);
    }

    @Override
    @Transactional
    public void deleteUserById(@Nonnull Integer id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new BusinessException("Error at deleting user by id: no such user");
        }
        userDao.delete(user);
    }

    private void checkUserForNullsWithoutPasswordAndRole(
            @Nonnull User user,
            @Nonnull EntityOperation operation) {
        if (user.getLogin() == null) {
            throw new BusinessException("Error at " + operation + " user:"
                    + " login is null");
        }
        UserProfile profile = user.getProfile();
        if (profile == null) {
            throw new BusinessException("Error at " + operation + " user:"
                    + " profile is null");
        }
        if (profile.getFirstName() == null) {
            throw new BusinessException("Error at " + operation + " user:"
                    + " first name is null");
        }
        if (profile.getLastName() == null) {
            throw new BusinessException("Error at " + operation + " user:"
                    + " last name is null");
        }
        if (profile.getBirthDate() == null) {
            throw new BusinessException("Error at " + operation + " user:"
                    + " birth date is null");
        }
    }

    private void handleUserByAuthorities(@Nonnull Integer id,
                                         @Nonnull User newUser,
                                         @Nonnull FullUserDetails currentUser) {
        RoleType currentRole = currentUser.getRoleType();
        if (RoleType.ROLE_ADMIN.equals(currentRole)) {
            if (newUser.getRoleType() == null) {
                throw new BusinessException("Error at updating user: role type"
                        + " is null");
            }
        } else if (RoleType.ROLE_USER.equals(currentRole)) {
            if (!id.equals(currentUser.getId())) {
                throw new BusinessException("Error at updating user: not admin"
                        + " can not update another user");
            }
            newUser.setRoleType(currentRole);
        } else {
            throw new BusinessException("Error at updating user: unknown role"
                    + " of current user");
        }
    }

    private void formEqualHandlersList(
            @Nonnull List<EqualQueryHandler<User, ?>> equalParameters,
            @Nullable String login,
            @Nullable RoleType type) {
        if (login != null) {
            equalParameters.add(new EqualQueryHandler<>(User_.login, login));
        }
        if (type != null) {
            equalParameters.add(new EqualQueryHandler<>(User_.roleType, type));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<User, Y>
    extractSortField(@Nullable UserSortCriterion criterion) {
        if (UserSortCriterion.LOGIN.equals(criterion)) {
            return (SingularAttribute<User, Y>) User_.login;
        } else if (UserSortCriterion.ROLE.equals(criterion)) {
            return (SingularAttribute<User, Y>) User_.roleType;
        } else if (!UserSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding users by criterion:"
                    + " invalid criterion");
        }
        return null;
    }
}
