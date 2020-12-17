package com.senla.training.knowledgeservice.dao.user;

import com.senla.training.knowledgeservice.dao.GenericDao;
import com.senla.training.knowledgeservice.model.user.User;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public interface UserDao extends GenericDao<User, Integer> {

    @CheckForNull
    User findUserByLogin(@Nullable String login);
}
