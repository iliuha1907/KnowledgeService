package com.senla.training.knowledgeservice.service.security;

import com.senla.training.knowledgeservice.model.user.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class FullUserDetails extends User {

    private final Integer id;
    private final RoleType roleType;

    public FullUserDetails(String username,
                           String password,
                           Collection<? extends GrantedAuthority> authorities,
                           Integer id,
                           RoleType roleType) {
        super(username, password, authorities);
        this.id = id;
        this.roleType = roleType;
    }

    public Integer getId() {
        return id;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}
