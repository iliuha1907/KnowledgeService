package com.senla.training.knowledgeservice.model.user;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.model.review.CourseReview;
import com.senla.training.knowledgeservice.model.review.LessonReview;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Basic
    @Column(name = "login", nullable = false, length = 45)
    private String login;
    @Basic
    @Column(name = "password", nullable = false)
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", nullable = false, length = 45)
    private RoleType roleType;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "id",
            nullable = false)
    private UserProfile profile;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",
            fetch = FetchType.LAZY)
    private Set<CourseSubscription> courseSubscriptions = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",
            fetch = FetchType.LAZY)
    private Set<LessonSubscription> lessonSubscriptions = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",
            fetch = FetchType.LAZY)
    private Set<LessonReview> lessonReviews = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user",
            fetch = FetchType.LAZY)
    private Set<CourseReview> courseReviews = new HashSet<>();

    public User() {
    }

    public User(String login,
                String password,
                RoleType roleType,
                UserProfile profile) {
        this.login = login;
        this.password = password;
        this.roleType = roleType;
        this.profile = profile;
    }

    public User(Integer id,
                String login,
                String password,
                RoleType roleType,
                UserProfile profile) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.roleType = roleType;
        this.profile = profile;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public Set<LessonSubscription> getLessonSubscriptions() {
        return lessonSubscriptions;
    }

    public Set<CourseSubscription> getCourseSubscriptions() {
        return courseSubscriptions;
    }

    public Set<LessonReview> getLessonReviews() {
        return lessonReviews;
    }

    public Set<CourseReview> getCourseReviews() {
        return courseReviews;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public void setLessonSubscriptions(
            Set<LessonSubscription> lessonSubscriptions) {
        this.lessonSubscriptions = lessonSubscriptions;
    }

    public void setCourseSubscriptions(
            Set<CourseSubscription> courseSubscriptions) {
        this.courseSubscriptions = courseSubscriptions;
    }

    public void setLessonReviews(Set<LessonReview> lessonReviews) {
        this.lessonReviews = lessonReviews;
    }

    public void setCourseReviews(Set<CourseReview> courseReviews) {
        this.courseReviews = courseReviews;
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
                && roleType == user.roleType
                && Objects.equals(profile, user.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), login, password,
                roleType, profile);
    }

    @Override
    public String toString() {
        return "User{"
                + "login='" + login
                + ", password='" + password
                + ", roleType=" + roleType
                + ", profile=" + profile
                + ", id=" + id + '}';
    }
}
