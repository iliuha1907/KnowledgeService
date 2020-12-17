package com.senla.training.knowledgeservice.model.lesson;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.review.LessonReview;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "lessons")
public class Lesson extends AbstractEntity {

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;
    @Basic
    @Column(name = "description", nullable = false)
    private String description;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false, length = 45)
    private LessonType type;
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    private Course course;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lesson",
            fetch = FetchType.LAZY)
    private Set<LessonSubscription> lessonSubscriptions = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lesson",
            fetch = FetchType.LAZY)
    private Set<LessonReview> lessonReviews = new HashSet<>();

    public Lesson() {
    }

    public Lesson(String title,
                  String description,
                  LessonType type,
                  Course course) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.course = course;
    }

    public Lesson(Integer id,
                  String title,
                  String description,
                  LessonType type,
                  Course course) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LessonType getType() {
        return type;
    }

    public Course getCourse() {
        return course;
    }

    public Set<LessonSubscription> getLessonSubscriptions() {
        return lessonSubscriptions;
    }

    public Set<LessonReview> getLessonReviews() {
        return lessonReviews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setLessonSubscriptions(
            Set<LessonSubscription> lessonSubscriptions) {
        this.lessonSubscriptions = lessonSubscriptions;
    }

    public void setLessonReviews(Set<LessonReview> lessonReviews) {
        this.lessonReviews = lessonReviews;
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
        Lesson lesson = (Lesson) o;
        return Objects.equals(title, lesson.title)
                && Objects.equals(description, lesson.description)
                && type == lesson.type
                && Objects.equals(course, lesson.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, type, course);
    }

    @Override
    public String toString() {
        return "Lesson{"
                + "title='" + title
                + ", description='" + description
                + ", type=" + type
                + ", course=" + course
                + ", id=" + id + '}';
    }
}
