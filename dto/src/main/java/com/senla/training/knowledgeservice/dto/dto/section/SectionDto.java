package com.senla.training.knowledgeservice.dto.dto.section;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;

import java.util.Objects;

public class SectionDto extends AbstractDto {

    private String title;
    private String description;
    private Integer topicId;

    public SectionDto() {
        super(null);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
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
        SectionDto that = (SectionDto) o;
        return Objects.equals(title, that.title)
                && Objects.equals(description, that.description)
                && Objects.equals(topicId, that.topicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, topicId);
    }

    @Override
    public String toString() {
        return "SectionDto{"
                + "title='" + title
                + ", description='" + description
                + ", topicId=" + topicId
                + '}';
    }
}
