package com.senla.training.knowledgeservice.model.review;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.user.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "lesson_reviews")
public class LessonReview extends AbstractEntity {

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
    @JoinColumn(name = "lesson_id", referencedColumnName = "id", nullable = false)
    private Lesson lesson;

    public LessonReview() {
    }

    public LessonReview(String message,
                        Date reviewDate,
                        User user,
                        Lesson lesson) {
        this.message = message;
        this.reviewDate = reviewDate;
        this.user = user;
        this.lesson = lesson;
    }

    public LessonReview(Integer id,
                        String message,
                        Date reviewDate,
                        User user,
                        Lesson lesson) {
        this.id = id;
        this.message = message;
        this.reviewDate = reviewDate;
        this.user = user;
        this.lesson = lesson;
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

    public Lesson getLesson() {
        return lesson;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
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
        LessonReview that = (LessonReview) o;
        return Objects.equals(message, that.message)
                && Objects.equals(reviewDate, that.reviewDate)
                && Objects.equals(user, that.user)
                && Objects.equals(lesson, that.lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, reviewDate, user, lesson);
    }

    @Override
    public String toString() {
        return "LessonReview{"
                + "message='" + message
                + ", reviewDate=" + reviewDate
                + ", user=" + user
                + ", lesson=" + lesson
                + ", id=" + id + '}';
    }
}
