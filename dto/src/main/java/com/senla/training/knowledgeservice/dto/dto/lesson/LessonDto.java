package com.senla.training.knowledgeservice.dto.dto.lesson;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;
import com.senla.training.knowledgeservice.model.lesson.LessonType;

import java.util.Objects;

public class LessonDto extends AbstractDto {

    private String title;
    private String description;
    private LessonType type;
    private Integer courseId;

    public LessonDto() {
        super(null);
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

    public Integer getCourseId() {
        return courseId;
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

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
        LessonDto lessonDto = (LessonDto) o;
        return Objects.equals(title, lessonDto.title)
                && Objects.equals(description, lessonDto.description)
                && type == lessonDto.type
                && Objects.equals(courseId, lessonDto.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, type, courseId);
    }

    @Override
    public String toString() {
        return "LessonDto{"
                + "title='" + title
                + ", description='" + description
                + ", type=" + type
                + ", courseId=" + courseId
                + '}';
    }
}
