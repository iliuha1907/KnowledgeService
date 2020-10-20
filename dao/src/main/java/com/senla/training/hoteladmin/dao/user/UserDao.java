package com.senla.training.hoteladmin.dao.user;

import com.senla.training.hoteladmin.dao.GenericDao;
import com.senla.training.hoteladmin.model.user.User;

public interface UserDao extends GenericDao<User, Integer> {

    User getUserByLogin(String login);
}
