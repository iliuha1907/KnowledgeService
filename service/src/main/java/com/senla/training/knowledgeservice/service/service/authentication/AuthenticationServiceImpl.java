package com.senla.training.knowledgeservice.service.service.authentication;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public void authenticateUser(@Nullable String userLogin,
                                 @Nonnull String userPassword) {
        User user = userDao.findUserByLogin(userLogin);
        if (user == null) {
            throw new BusinessException("Error at authenticating user: no such user");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLogin, userPassword));
    }
}
