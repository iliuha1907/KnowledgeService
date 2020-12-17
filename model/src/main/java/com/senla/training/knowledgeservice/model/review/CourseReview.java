package com.senla.training.knowledgeservice.model.review;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "course_reviews")
public class CourseReview extends AbstractEntity {

    @Basic
    @Column(name = "message", nullable = false)
    private String message;
    @Basic
    @Column(name = "review_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reviewDate;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;

    public CourseReview() {
    }

    public CourseReview(String message,
                        Date reviewDate,
                        User user,
                        Course course) {
        this.message = message;
        this.reviewDate = reviewDate;
        this.user = user;
        this.course = course;
    }

    public CourseReview(Integer id,
                        String message,
                        Date reviewDate,
                        User user,
                        Course course) {
        this.id = id;
        this.message = message;
        this.reviewDate = reviewDate;
        this.user = user;
        this.course = course;
    }

    public String getMessage() {
        return message;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
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
        CourseReview that = (CourseReview) o;
        return Objects.equals(message, that.message)
                && Objects.equals(user, that.user)
                && Objects.equals(course, that.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, user, course);
    }

    @Override
    public String toString() {
        return "CourseReview{"
                + "message='" + message
                + ", reviewDate=" + reviewDate
                + ", user=" + user
                + ", course=" + course
                + ", id=" + id + '}';
    }
}
