package com.senla.training.knowledgeservice.service.service.subscription.course;

import com.senla.training.knowledgeservice.common.sort.CourseSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface CourseSubscriptionService {

    void addSubscription(@Nonnull CourseSubscription courseSubscription);

    @Nonnull
    CourseSubscription findSubscriptionById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<CourseSubscription> findSubscriptions(
            @Nonnull CourseSubscriptionSortCriterion criterion,
            @Nullable Date beginDateForStart,
            @Nullable Date endDateForStart,
            @Nullable Date beginDateForEnd,
            @Nullable Date endDateForEnd,
            @Nullable Integer userId,
            @Nullable Integer courseId);

    void deleteSubscriptionById(@Nonnull Integer id);
}
