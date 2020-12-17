package com.senla.training.knowledgeservice.dto.dto.topic;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;

import java.util.Objects;

public class TopicDto extends AbstractDto {

    private String title;
    private String description;

    public TopicDto() {
        super(null);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
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
        TopicDto topicDto = (TopicDto) o;
        return Objects.equals(title, topicDto.title)
                && Objects.equals(description, topicDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description);
    }

    @Override
    public String toString() {
        return "TopicDto{"
                + "title='" + title
                + ", description='" + description
                + '}';
    }
}
