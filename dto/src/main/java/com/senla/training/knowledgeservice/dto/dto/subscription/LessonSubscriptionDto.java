package com.senla.training.knowledgeservice.dto.dto.subscription;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;

import java.util.Date;
import java.util.Objects;

public class LessonSubscriptionDto extends AbstractDto {

    private Date lessonDate;
    private Boolean tookPlace;
    private Integer userId;
    private Integer lessonId;
    private Integer teacherId;

    public LessonSubscriptionDto() {
        super(null);
    }

    public Date getLessonDate() {
        return lessonDate;
    }

    public Boolean getTookPlace() {
        return tookPlace;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getLessonId() {
        return lessonId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setLessonDate(Date lessonDate) {
        this.lessonDate = lessonDate;
    }

    public void setTookPlace(Boolean tookPlace) {
        this.tookPlace = tookPlace;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
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
        LessonSubscriptionDto that = (LessonSubscriptionDto) o;
        return Objects.equals(lessonDate, that.lessonDate)
                && Objects.equals(tookPlace, that.tookPlace)
                && Objects.equals(userId, that.userId)
                && Objects.equals(lessonId, that.lessonId)
                && Objects.equals(teacherId, that.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lessonDate, tookPlace,
                userId, lessonId, teacherId);
    }

    @Override
    public String toString() {
        return "LessonSubscriptionDto{"
                + "lessonDate=" + lessonDate
                + ", tookPlace=" + tookPlace
                + ", userId=" + userId
                + ", lessonId=" + lessonId
                + ", teacherId=" + teacherId
                + '}';
    }
}
