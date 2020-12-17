package com.senla.training.knowledgeservice.model.section;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.topic.Topic;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "sections")
public class Section extends AbstractEntity {

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;
    @Basic
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id", nullable = false)
    private Topic topic;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section",
            fetch = FetchType.LAZY)
    private Set<Course> courses = new HashSet<>();

    public Section() {
    }

    public Section(String title, String description, Topic topic) {
        this.title = title;
        this.description = description;
        this.topic = topic;
    }

    public Section(Integer id, String title, String description, Topic topic) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Topic getTopic() {
        return topic;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
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
        Section section = (Section) o;
        return Objects.equals(title, section.title)
                && Objects.equals(description, section.description)
                && Objects.equals(topic, section.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, topic);
    }

    @Override
    public String toString() {
        return "Section{"
                + "title=" + title
                + ", description='" + description
                + ", topic=" + topic
                + ", id=" + id + '}';
    }
}
