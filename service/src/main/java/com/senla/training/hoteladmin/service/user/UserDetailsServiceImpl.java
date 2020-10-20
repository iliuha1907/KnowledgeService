package com.senla.training.hoteladmin.service.user;

import com.senla.training.hoteladmin.dao.user.UserDao;
import com.senla.training.hoteladmin.model.user.User;
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
        User user = userDao.getUserByLogin(login);

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                new HashSet<GrantedAuthority>(Collections.singletonList(
                        new SimpleGrantedAuthority(user.getRole().getType().name()))));
    }
}
