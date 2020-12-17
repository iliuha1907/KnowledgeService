package com.senla.training.knowledgeservice.service.service.user;

import com.senla.training.knowledgeservice.common.sort.UserSortCriterion;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface UserService {

    void addUser(@Nonnull User user);

    @Nonnull
    <Y extends Comparable<? super Y>> List<User> findUsers(
            @Nonnull UserSortCriterion criterion,
            @Nullable String login,
            @Nullable RoleType type);

    @Nonnull
    User findUserById(@Nonnull Integer id);

    @Nonnull
    User findUserByLogin(@Nullable String login);

    void updateUser(@Nonnull User user, @Nonnull Integer id);

    void deleteUserById(@Nonnull Integer id);
}
