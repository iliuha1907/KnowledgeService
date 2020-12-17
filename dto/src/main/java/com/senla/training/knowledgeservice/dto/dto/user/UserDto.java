package com.senla.training.knowledgeservice.dto.dto.user;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;
import com.senla.training.knowledgeservice.model.user.RoleType;

import java.util.Date;
import java.util.Objects;

public class UserDto extends AbstractDto {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private RoleType roleType;
    private Date birthDate;

    public UserDto() {
        super(null);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
        UserDto userDto = (UserDto) o;
        return Objects.equals(login, userDto.login)
                && Objects.equals(password, userDto.password)
                && Objects.equals(firstName, userDto.firstName)
                && Objects.equals(lastName, userDto.lastName)
                && roleType == userDto.roleType
                && Objects.equals(birthDate, userDto.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password, firstName, lastName,
                roleType, birthDate);
    }

    @Override
    public String toString() {
        return "UserDto{"
                + "login='" + login
                + ", password='" + password
                + ", firstName='" + firstName
                + ", lastName='" + lastName
                + ", roleType=" + roleType
                + ", birthDate=" + birthDate
                + '}';
    }
}
