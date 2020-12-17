package com.senla.training.knowledgeservice.model.subscription.course;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "course_subscriptions")
public class CourseSubscription extends AbstractEntity {

    @Basic
    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic
    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;

    public CourseSubscription() {
    }

    public CourseSubscription(Date startDate, Date endDate,
                              User user, Course course) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.course = course;
    }

    public CourseSubscription(Integer id, Date startDate, Date endDate,
                              User user, Course course) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.course = course;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
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
        CourseSubscription that = (CourseSubscription) o;
        return Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(user, that.user)
                && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, endDate, user, course);
    }

    @Override
    public String toString() {
        return "CourseSubscription{"
                + "startDate=" + startDate
                + ", endDate=" + endDate
                + ", user=" + user
                + ", course=" + course
                + ", id=" + id + '}';
    }
}
