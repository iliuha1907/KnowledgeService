package com.senla.training.hoteladmin.service.user;

import com.senla.training.hoteladmin.model.user.User;

public interface UserService {

    User getUserByLogin(String login);
}
