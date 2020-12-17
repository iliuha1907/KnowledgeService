package com.senla.training.knowledgeservice.model.subscription.lesson;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.teacher.Teacher;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "lesson_subscriptions")
public class LessonSubscription extends AbstractEntity {

    @Basic
    @Column(name = "lesson_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date lessonDate;
    @Basic
    @Column(name = "took_place", nullable = false)
    private Boolean tookPlace;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "id", nullable = false)
    private Lesson lesson;
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)
    private Teacher teacher;

    public LessonSubscription() {
    }

    public LessonSubscription(Date lessonDate,
                              Boolean tookPlace,
                              User user,
                              Lesson lesson,
                              Teacher teacher) {
        this.lessonDate = lessonDate;
        this.tookPlace = tookPlace;
        this.user = user;
        this.lesson = lesson;
        this.teacher = teacher;
    }

    public LessonSubscription(Integer id,
                              Date lessonDate,
                              Boolean tookPlace,
                              User user,
                              Lesson lesson,
                              Teacher teacher) {
        this.id = id;
        this.lessonDate = lessonDate;
        this.tookPlace = tookPlace;
        this.user = user;
        this.lesson = lesson;
        this.teacher = teacher;
    }

    public Date getLessonDate() {
        return lessonDate;
    }

    public Boolean getTookPlace() {
        return tookPlace;
    }

    public User getUser() {
        return user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setLessonDate(Date lessonDate) {
        this.lessonDate = lessonDate;
    }

    public void setTookPlace(Boolean tookPlace) {
        this.tookPlace = tookPlace;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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
        LessonSubscription that = (LessonSubscription) o;
        return Objects.equals(lessonDate, that.lessonDate)
                && Objects.equals(tookPlace, that.tookPlace)
                && Objects.equals(user, that.user)
                && Objects.equals(lesson, that.lesson)
                && Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lessonDate, tookPlace,
                user, lesson, teacher);
    }

    @Override
    public String toString() {
        return "LessonSubscription{"
                + "lessonDate=" + lessonDate
                + ", tookPlace=" + tookPlace
                + ", user=" + user
                + ", lesson=" + lesson
                + ", teacher=" + teacher
                + ", id=" + id + '}';
    }
}
