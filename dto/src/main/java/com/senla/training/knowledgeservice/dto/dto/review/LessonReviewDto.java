package com.senla.training.knowledgeservice.dto.dto.review;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;

import java.util.Date;
import java.util.Objects;

public class LessonReviewDto extends AbstractDto {

    private String message;
    private Date reviewDate;
    private Integer userId;
    private Integer lessonId;

    public LessonReviewDto() {
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

    public Integer getLessonId() {
        return lessonId;
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

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
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
        LessonReviewDto that = (LessonReviewDto) o;
        return Objects.equals(message, that.message)
                && Objects.equals(reviewDate, that.reviewDate)
                && Objects.equals(userId, that.userId)
                && Objects.equals(lessonId, that.lessonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), message, reviewDate, userId, lessonId);
    }

    @Override
    public String toString() {
        return "LessonReviewDto{"
                + "message='" + message
                + ", reviewDate=" + reviewDate
                + ", userId=" + userId
                + ", lessonId=" + lessonId
                + '}';
    }
}
