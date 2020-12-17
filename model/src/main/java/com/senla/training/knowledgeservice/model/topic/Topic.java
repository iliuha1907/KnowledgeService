package com.senla.training.knowledgeservice.model.topic;

import com.senla.training.knowledgeservice.model.AbstractEntity;
import com.senla.training.knowledgeservice.model.section.Section;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic extends AbstractEntity {

    @Basic
    @Column(name = "title", nullable = false, length = 45)
    private String title;
    @Basic
    @Column(name = "description", nullable = false)
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "topic", fetch = FetchType.LAZY)
    private Set<Section> sections = new HashSet<>();

    public Topic() {
    }

    public Topic(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Topic(Integer id, String title, String description) {
        this.title = title;
        this.id = id;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<Section> getSections() {
        return sections;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSections(Set<Section> sections) {
        this.sections = sections;
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
        Topic topic = (Topic) o;
        return Objects.equals(title, topic.title)
                && Objects.equals(description, topic.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description);
    }

    @Override
    public String toString() {
        return "Topic{"
                + "title='" + title
                + ", description='" + description
                + ", id=" + id + '}';
    }
}
