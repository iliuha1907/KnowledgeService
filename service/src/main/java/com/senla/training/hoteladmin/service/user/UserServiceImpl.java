package com.senla.training.hoteladmin.service.user;

import com.senla.training.hoteladmin.dao.user.UserDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByLogin(String login) {
        User user = userDao.getUserByLogin(login);
        if (user == null) {
            throw new BusinessException("Error getting user by login: No such entity!");
        }

        return user;
    }
}
