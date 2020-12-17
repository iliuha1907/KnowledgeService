package com.senla.training.knowledgeservice.dto.dto.review;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;

import java.util.Date;
import java.util.Objects;

public class CourseReviewDto extends AbstractDto {

    private String message;
    private Date reviewDate;
    private Integer userId;
    private Integer courseId;

    public CourseReviewDto() {
        super(null);
    }

    public String getMessage() {
        return message;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        CourseReviewDto that = (CourseReviewDto) o;
        return Objects.equals(message, that.message)
                && Objects.equals(reviewDate, that.reviewDate)
                && Objects.equals(userId, that.userId)
                && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, reviewDate, userId, courseId);
    }

    @Override
    public String toString() {
        return "CourseReviewDto{"
                + "message='" + message
                + ", reviewDate=" + reviewDate
                + ", userId=" + userId
                + ", courseId=" + courseId
                + '}';
    }
}
