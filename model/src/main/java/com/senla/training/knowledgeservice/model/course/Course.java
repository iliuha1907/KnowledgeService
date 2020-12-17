package com.senla.training.knowledgeservice.model.course;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.review.CourseReview;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course extends AbstractEntity {

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;
    @Basic
    @Column(name = "description", nullable = false)
    private String description;
    @Basic
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @Basic
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id",
            nullable = false)
    private Section section;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Lesson> lessons = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "course", fetch = FetchType.LAZY)
    private Set<CourseSubscription> courseSubscriptions = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "course", fetch = FetchType.LAZY)
    private Set<CourseReview> courseReviews = new HashSet<>();

    public Course() {
    }

    public Course(String title,
                  String description,
                  Integer duration,
                  BigDecimal price,
                  Section section) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.section = section;
    }

    public Course(Integer id,
                  String title,
                  String description,
                  Integer duration,
                  BigDecimal price,
                  Section section) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.section = section;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDuration() {
        return duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Section getSection() {
        return section;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public Set<CourseSubscription> getCourseSubscriptions() {
        return courseSubscriptions;
    }

    public Set<CourseReview> getCourseReviews() {
        return courseReviews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void setCourseSubscriptions(
            Set<CourseSubscription> courseSubscriptions) {
        this.courseSubscriptions = courseSubscriptions;
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
        Course course = (Course) o;
        return Objects.equals(title, course.title)
                && Objects.equals(description, course.description)
                && Objects.equals(duration, course.duration)
                && Objects.equals(price, course.price)
                && Objects.equals(section, course.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, duration,
                price, section);
    }

    @Override
    public String toString() {
        return "Course{"
                + "title=" + title
                + ", description='" + description
                + ", duration=" + duration
                + ", price=" + price
                + ", id=" + id + '}';
    }
}
