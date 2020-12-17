package com.senla.training.knowledgeservice.dto.dto.subscription;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;

import java.util.Date;
import java.util.Objects;

public class CourseSubscriptionDto extends AbstractDto {

    private Date startDate;
    private Date endDate;
    private Integer userId;
    private Integer courseId;

    public CourseSubscriptionDto() {
        super(null);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        CourseSubscriptionDto that = (CourseSubscriptionDto) o;
        return Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(userId, that.userId)
                && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startDate, endDate, userId, courseId);
    }

    @Override
    public String toString() {
        return "CourseSubscriptionDto{"
                + "startDate=" + startDate
                + ", endDate=" + endDate
                + ", userId=" + userId
                + ", courseId=" + courseId
                + '}';
    }
}
