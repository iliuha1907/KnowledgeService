package com.senla.training.knowledgeservice.dto.dto.course;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;

import java.math.BigDecimal;
import java.util.Objects;

public class CourseDto extends AbstractDto {

    private String title;
    private String description;
    private Integer duration;
    private BigDecimal price;
    private Integer sectionId;

    public CourseDto() {
        super(null);
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

    public Integer getSectionId() {
        return sectionId;
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

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
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
        CourseDto courseDto = (CourseDto) o;
        return Objects.equals(title, courseDto.title)
                && Objects.equals(description, courseDto.description)
                && Objects.equals(duration, courseDto.duration)
                && Objects.equals(price, courseDto.price)
                && Objects.equals(sectionId, courseDto.sectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description, duration,
                price, sectionId);
    }

    @Override
    public String toString() {
        return "CourseDto{"
                + "title='" + title
                + ", description='" + description
                + ", duration=" + duration
                + ", price=" + price
                + ", sectionId=" + sectionId
                + '}';
    }
}
