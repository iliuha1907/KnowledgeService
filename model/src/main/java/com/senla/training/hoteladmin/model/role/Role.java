package com.senla.training.hoteladmin.model.role;

import com.senla.training.hoteladmin.model.AbstractEntity;
import com.senla.training.hoteladmin.model.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends AbstractEntity {

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role_type", nullable = false, length = 45)
    private RoleType type;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public RoleType getType() {
        return type;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setType(RoleType type) {
        this.type = type;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
        Role role = (Role) o;
        return type == role.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
