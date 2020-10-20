package com.senla.training.hoteladmin.model.user;

import com.senla.training.hoteladmin.model.AbstractEntity;
import com.senla.training.hoteladmin.model.role.Role;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Basic
    @Column(name = "login", nullable = false, length = 45)
    private String login;
    @Basic
    @Column(name = "password", nullable = false, length = 45)
    private String password;
    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id", nullable = false)
    private Role role;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(login, user.login)
                && Objects.equals(password, user.password)
                && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password, role);
    }
}
