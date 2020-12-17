package com.senla.training.knowledgeservice.service.security;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userDao.findUserByLogin(login);
        if (user == null) {
            throw new BusinessException("Error at loading user by username: no such user");
        }
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPassword(), new HashSet<GrantedAuthority>(
                Collections.singletonList(new SimpleGrantedAuthority(
                        user.getRoleType().name()))));
    }
}
